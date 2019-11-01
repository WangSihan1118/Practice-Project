#!/usr/bin/env python

import sys, os

# get the ID of the input file, the metric being tested and the action to be taken
# on the file based on the named metric
def get_inputs() :
  metric_list = ["min", "mean", "median", "harmonic_mean"]
  action_list = ["list", "correlation"]
  infilename = input("Enter name of file containing World Happiness computation data: ")
  if os.path.isfile(infilename) :
    infile = open(infilename, 'r')
  else:
    print("The file {0:s} is not present".format(infilename))
    return(None, None, None, False)

  metric = input("Choose metric to be tested from: min, mean, median, harmonic_mean ")
  if not metric in metric_list :
    print("The specified metric {0:s} is not in the list of known metrics {1:s}".format(metric, str(metric_list)))
    return(None, None, None, False)

  action = input("Chose action to be performed on the data using the specified metric. Options are list, correlation ")
  if not action in action_list :
    print("The specified action {0:s} is not in the list of allowed actions {1:s}".format(action, str(action_list)))
    return(None, None, None, False)
  return(infile, metric, action, True)
  
# read in file. Each country is a list, building a list of lists
# Ignore columnn headers in the first row
def read_in_file(infile) :
  country_data = []
  ncols = len(infile.readline().split(','))
  for line in infile :
    fields = line.strip().split(',')
    newfields = [fields[0]]
    for datum in fields[1:] :
      if datum == "" :
        newfields.append(None)
      else:
        newfields.append(float(datum))
    country_data.append(newfields)
  return(country_data, ncols)

# find min and max of a list, ignoring None values
def minmax(L) :
  minval = maxval = None
  for val in L :
    if val != None :
      if minval == None or val < minval :
        minval = val
      if maxval == None or val > maxval :
        maxval = val
  return(minval, maxval)

def median(L) :
  nonvalist = []
  for i in L :
    if i != None :
      nonvalist.append(i)
  nonvalist.sort()
  mid = len(nonvalist) // 2
  if len(nonvalist) % 2 == 0 :
    medval = (nonvalist[mid-1]+nonvalist[mid])/2
  else:
    medval = nonvalist[mid]
  return(medval)

def mean(L) :
  N = 0
  sum = 0
  for i in L :
    if i != None :
      N += 1
      sum += i
  return(sum/N)

def harmonic_mean(L) :
  N = 0
  invsum = 0
  for i in L :
    if i != None and i != 0 :
      N += 1
      invsum += 1/i
  return(N/invsum)

# get the min and max values for each column after the second (which we don't touch) 
def get_col_min_max(country_data, ncols) :
  col_min_max_list = [ None, None ]
  for colno in range(2,ncols) :
    collist = []
    for rowno in range(len(country_data)) :
      collist.append(country_data[rowno][colno])
    minval, maxval = minmax(collist)
    col_min_max_list.append((minval, maxval))
  return(col_min_max_list)
  
"""
create a new version of the country_data with each value other that first cols normalised
to a floating point value 0 .. 1, with 0 being the min in a column and 1 the maximum value
in that column
"""

def normalise_data(country_data, col_min_max_list, ncols) : 
  new_country_data = []
  for data in country_data :
    new_data = data[:2]
    for i in range(2,ncols) :
      val = data[i]
      if val == None :
        new_data.append(None)
      else:
        new_data.append((val-col_min_max_list[i][0])/(col_min_max_list[i][1]-col_min_max_list[i][0]))
    new_country_data.append(new_data)
  return(new_country_data)


# Different Happiness metrics are being tested on the normalised data (omiting the
# country name (of course), but also metric computed by the study (second column)
def country_score(country_data, metric) :
  score_country_list = []
  for data in country_data :  # normalised this time
    country = data[0]
    # score = minmax(data[1:])[0]
    if metric == "min" :
      score = minmax(data[2:])[0]
    elif metric == "median" :
      score = median(data[2:])
    elif metric == "mean" :
      score = mean(data[2:])
    elif metric == "harmonic_mean" :
      score = harmonic_mean(data[2:])
    score_country_list.append((score,country))
  score_country_list.sort(reverse=True)
  return(score_country_list)

def get_sorted_WHR(country_data) :
  WHR_list = []
  for data in country_data  :
    WHR_list.append((data[1], data[0]))
  WHR_list.sort(reverse=True)
  return(WHR_list)

# given a list of score_country pairs, strip off the scores, return a list
# of just the countries
def strip_scores(pair_list) :
  country_list = []
  for score, country in pair_list :
    country_list.append(country) 
  return(country_list)


# spearman assumes that list1 and list2 have been sorted the same way
def spearman(list1, list2) :
  rank_diffs = 0
  for i in range(len(list1)) :
    country = list1[i]
    other_loc = list2.index(country)
    d = i - other_loc
    rank_diffs += d * d
  N = len(list1)
  rho = 1 - 6 * rank_diffs/(N*(N*N-1))
  return(rho)
  

def print_results(score_country_list) :
  for score, country in score_country_list:
    print("{0:s}\t{1:0.4f}".format(country, score))


def main():
  infile, metric, action, inputs_okay = get_inputs()
  if not inputs_okay :
    return
  country_data, ncols = read_in_file(infile)
  if country_data == [] :
    print("Data file was empty")
    return
  col_min_max_list = get_col_min_max(country_data, ncols)
  new_country_data = normalise_data(country_data, col_min_max_list, ncols)
  sorted_score_country_list = country_score(new_country_data, metric)
  if action == "list" :
    print("Ranked list of countries' happiness scores based the {0:s} metric".format(metric))
    print_results(sorted_score_country_list)
  else:
    sorted_WHRscore_country_list = get_sorted_WHR(country_data)
    rho = spearman(strip_scores(sorted_score_country_list), strip_scores(sorted_WHRscore_country_list))
    print("The correlation coefficient between the study ranking and the ranking using the {0:s} metric is {1:0.4f}".format(metric, rho))
  return

if __name__ == "__main__" :
  main()

