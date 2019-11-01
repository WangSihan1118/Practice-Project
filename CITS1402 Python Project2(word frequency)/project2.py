import math
def stripAllPunctuation(string):
    punctuations = '\'\"#\$%&()*+,-./:;<=>?@[\\]^_`{|}~'
    for punctuation in punctuations:
        string = string.replace(punctuation,"")
    return string

def stripPunctuation(string):
    punctuations = '\'\"#\$%&()*+,-/:;<=>?@[\\]^_`{|}~'
    for punctuation in punctuations:
        string = string.replace(punctuation,"")
    return string

def switchToFullstop(string):
    punctuations = "!?.?"
    for punctuation in punctuations:
        string = string.replace(punctuation,".")
    return string
            
def stripLetters(string):
    letters = "abcdefghijklmnopqrstuvwxyz"
    for letter in letters:
        string = string.replace(letter,"")
    return string

def punctuation(txtfile):#count,;'-
    txtlist = txtfile.split()
    '''remove sides punctuation'''
    punctuation_txt1 = []
    for word in txtlist:
        word = word.strip(".?!:\"\' ")
        punctuation_txt1.append(word)
   
    '''remove letters'''
    punctuation_txt2 = []
    plist = ["\"","--",""]
    for word in punctuation_txt1:
        punctuation = stripLetters(word)
        for p in plist:
            punctuation = punctuation.replace(p,'')
        if punctuation != "":
            punctuation_txt2.append(punctuation)
  
    '''remove space'''
    punctuation_txt3 = []
    for punctuation in punctuation_txt2:   
        punctuation = punctuation.strip(' ')
        punctuation_txt3.append(punctuation)
    punctuation_txt4 = []
    for m in punctuation_txt3:
        for n in m:
            punctuation_txt4.append(n)
            
    punctuation_list = [",",";","-","'"]
    punctuation_dict = {}
    for punctuation in punctuation_list:
        punctuation_dict[punctuation] = 0
    for punctuation in punctuation_txt4:
        if punctuation in punctuation_dict:
            punctuation_dict[punctuation] = punctuation_dict[punctuation] + 1
    return punctuation_dict

def unigrams(txtfile):
    unigramstxt = stripAllPunctuation(txtfile)
    unigrams_list = unigramstxt.split()
        
    unigrams_dic = {}
    for word in unigrams_list:
        unigrams_dic[word] = unigrams_dic.get(word,0) + 1
    return unigrams_dic

def conjunctions(txtfile):
    txtfile = stripAllPunctuation(txtfile)
    txtfile = txtfile.split()
    conjunctions_txt = []
    ''' remove punctuation(,)'''
    for word in txtfile:
        conjunctions_txt.append(word.strip(",\'\".-?!"))
     
    conjunctions_list = ["also", "although", "and", "as", "because", "before", "but", "for", "if", "nor", "of",
"or", "since", "that", "though", "until", "when", "whenever", "whereas",
"which", "while", "yet"]
    conjunctions_dict = {}
    for word in conjunctions_list:
        conjunctions_dict[word] = 0
    for word in conjunctions_txt:
        if word in conjunctions_dict:
            conjunctions_dict[word] = conjunctions_dict[word] + 1
    return conjunctions_dict

def averageWord(textfile):
    '''remove all the other expect FULLSTOP'''
    textfile = switchToFullstop(textfile)#type string
    textfile = stripPunctuation(textfile)
    sentence_list = textfile.split(".")#type list
    sentence_list1 = []
    for sentence in sentence_list:
        sentence = sentence.replace("\n"," ")
        sentence_list1.append(sentence)
    if " " in sentence_list1:
        sentence_list1.remove(" ")
    
    '''count how many words'''
    wordsum = 0
    for sentence in sentence_list1:
        wordcount = len(sentence.split())
        wordsum += wordcount
        
    '''count how many sentences'''
    sentencecount = len(sentence_list1)
    
    averageWord = round(wordsum / sentencecount,4)
    
    averageWord_dict = {'words_per_sentence':averageWord}
    return averageWord_dict


def averageSentence(textfile):
    textfile = switchToFullstop(textfile)#type string
    textfile = stripPunctuation(textfile)
    paragraph_list = textfile.split("\n\n")
    paragraph_list1 = []
    for paragraph in paragraph_list:
        paragraph = paragraph.replace("\n"," ")
        paragraph_list1.append(paragraph)
        
    if " " in paragraph_list1:
        paragraph_list1.remove(" ")
    
    '''count how many sentence'''
    fullstop_dict = {".":0}
    for sentence in paragraph_list1:
        for digit in sentence:
            if digit ==".":
               fullstop_dict["."] =  fullstop_dict['.'] + 1
        
    sentencesum = fullstop_dict["."]
    '''count how many paragraph'''
    paragraphcount = len(paragraph_list1)
    
    averageSentence = round(sentencesum / paragraphcount,4)
    
    averageSentence_dict = {'sentences_per_par':averageSentence}
    return averageSentence_dict

    
def composite(txtfile):
    conjunctions_dict = conjunctions(txtfile)
    punctuation_dict = punctuation(txtfile)
    averageWord_dict = averageWord(txtfile)
    averageSentence_dict = averageSentence(txtfile)
    
    conjunctions_dict.update(punctuation_dict)
    conjunctions_dict.update(averageWord_dict)
    conjunctions_dict.update(averageSentence_dict)
    
    return conjunctions_dict

def analyzeFile(filename):
    file = open(filename,"r")
    filetxt = file.read().lower()
    filetxt = filetxt.replace("--"," ")
    return filetxt

def main(textfile1,textfile2,feature):
    try:
        filetxt1 = analyzeFile(textfile1)
        filetxt2 = analyzeFile(textfile2)
        score = 0
            
        if feature == "punctuation":
            punctuation_dict1 = punctuation(filetxt1)
            punctuation_dict2 = punctuation(filetxt2)
            keyslist = punctuation_dict1.keys()
            for key in keyslist:
                if key in punctuation_dict2:
                    score = score + (punctuation_dict1[key] - punctuation_dict2[key])**2
            score = math.sqrt(score)
            score = round(score,4)
            return score,punctuation_dict1,punctuation_dict2
            
        elif feature == "unigrams":
            unigrams_dict1 = unigrams(filetxt1)
            unigrams_dict2 = unigrams(filetxt2)
            keyslist = unigrams_dict1.keys()
            for key in keyslist:
                if key in unigrams_dict2:
                    score = score + (unigrams_dict1[key] - unigrams_dict2[key])**2
            score = math.sqrt(score)
            score = round(score,4)
            return score,unigrams_dict1,unigrams_dict2
            
        elif feature == "conjunctions":
            conjunctions_dict1 = conjunctions(filetxt1)
            conjunctions_dict2 = conjunctions(filetxt2)
            keyslist = conjunctions_dict1.keys()
            for key in keyslist:
                if key in conjunctions_dict2:
                    score = score + (conjunctions_dict1[key] - conjunctions_dict2[key])**2
            score = math.sqrt(score)
            score = round(score,4)
            return score,conjunctions_dict1,conjunctions_dict2
            
        elif feature == "composite":
            composite_dict1 = composite(filetxt1)
            composite_dict2 = composite(filetxt2)
            keyslist = composite_dict1.keys()
            for key in keyslist:
                if key in composite_dict2:
                    score = score + (composite_dict1[key] - composite_dict2[key])**2
            score = math.sqrt(score)
            score = round(score,4)
            return score,composite_dict1,composite_dict2
    except Exception as e:
        print(e)