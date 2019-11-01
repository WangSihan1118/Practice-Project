import os
def main():  
    def normalize(targetList):
        targetList1 = []
        targetList2 = []
        for i in targetList:
            if i != "":
                targetList1.append(float(i))
            else:
                targetList1.append(None)
        for i in targetList1:
            if i is not None:
                i = (i-min([i for i in targetList1 if i is not None])) / (max([i for i in targetList1 if i is not None]) - min([i for i in targetList1 if i is not None]))
                targetList2.append(float(i))
            else:
                targetList2.append(None)
        return(targetList2)

    def median(targetList):
        sorted(targetList)
        length = len(targetList)
        if length % 2 == 0:
            median = (targetList[length//2 - 1] + targetList[(length//2)]) / 2
        else:
            median = targetList[(length - 1)//2]
        return median
        
        
    def mean(targetList):
        for i in targetList:
            mean = sum([i for i in targetList if i is not None]) / len([i for i in targetList if i is not None])
        return mean

    def harmonicmean(targetList):
        reciprocalList = []
        targetList = filter(None, targetList)
        for i in targetList:
            i = 1/i
            reciprocalList.append(i)
        harmonicmean = len(reciprocalList) / sum(reciprocalList) 
        return harmonicmean

    def maximum(targetList):
        maximum = max([i for i in targetList if i is not None])
        return maximum

    def minimum(targetList):
        minimum = min([i for i in targetList if i is not None])
        return minimum

    def combine(list1,list2):
        combinedList = [list(n) for n in zip(list1,list2)]
        return combinedList

    def spearman(targetList):
        sortedLifeLadder = sorted(lifeLadder, reverse=True)
        sortedtargetList = sorted(targetList, reverse=True)

        index1 = [sortedLifeLadder.index(i)for i in lifeLadder]
        index2 = [sortedtargetList.index(i)for i in targetList]

        sum = 0
        n = len(lifeLadder) 
        for i in range(n):
            sum += (index1[i] - index2[i])**2
        spearman = 1- float(6*sum) / (n * (n**2 - 1))
        return spearman

    filename = input("Enter name of file containing World Happiness computation data:")
    if not os.path.isfile(filename) :
        return(None)
    metric = input("Choose metric to be tested from: min, mean, median, harmonic_mean mean")
    action = input("Chose action to be performed on the data using the specified metric. Options are list, correlation list")
    file = open(filename,"r")#Open the file
    initialList = []
    for line in file:#Convert csv. to original list
        s = line.strip("\n")
        lineList = s.split(",")
        initialList.append(lineList)
    del(initialList[0])#delete the title

    #First two columne
    country = [i[0]for i in initialList]
    lifeLadder =[float (i) for i in [i[1] for i in initialList]]

    socialSupport = [i[3] for i in initialList]
    lifeExpectancy = [i[4] for i in initialList]
    freedom = [i[5] for i in initialList]
    generosity = [i[6] for i in initialList]
    confidence = [i[7] for i in initialList]
           
    #normalized the data
    normalizedSocialSupport = normalize(socialSupport)
    normalizedLifeExpectancy =normalize(lifeExpectancy)
    normalizedFreedom = normalize(freedom)
    normalizedGenerosity = normalize(generosity)
    normalizedConfidence = normalize(confidence)
    normalizedReference = [country] + [lifeLadder] + [normalizedSocialSupport] +[normalizedLifeExpectancy] + [normalizedFreedom] + [normalizedGenerosity] + [normalizedConfidence]

    referenceCountry = []
    for i in country:
        referenceCountry.append(i)

    normalizedList = []
    n = 0
    while n != len(referenceCountry) :
        normalizedList.append([x[n]for x in normalizedReference])
        n = n + 1

    normalizedValue = [i[2:]for i in normalizedList]

    #caculate the metric
    metricList = []
    if metric == "max":
        for i in normalizedValue:
            metricList.append(maximum(i))
    elif metric == "min":
        for i in normalizedValue:
            metricList.append(minimum(i))
    elif metric == "mean":
        for i in normalizedValue:
            metricList.append(mean(i))
    elif metric == "harmonic_mean":
        for i in normalizedValue:
            metricList.append(harmonicmean(i))

    if action == "list":
        print("Ranked list of countries' happiness scores based the "+metric+" metric")
        combinedList = [list(n) for n in zip(referenceCountry,metricList)]
        sortedCombinedList = sorted(combinedList, key=lambda i:i[1],reverse = True)
        for i in sortedCombinedList:
            print(i[0],i[1])
    elif action == "correlation list":
        speamanvalue = spearman(metricList)
        print("The correlation coefficient between the study ranking and the ranking using the "+metric+" metric is: ",speamanvalue)