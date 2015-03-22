import scipy.io
import math


data = scipy.io.loadmat('C:\\Users\\Alexandr\\Dropbox\\2b\\gtzan.mat')

def main():
    
    #train = [1,2,3]
    #test = [1,2,3]
    #classes = [10,9,8]
    train = data['fold1_features']
    test = data['fold2_features']
    classes = data['fold1_classes']
    testClasses = data['fold2_classes']
    
    # print missclassification
    
def kNN(k, training, classes, test):
    classification = [0]*len(test)
    for i, z in enumerate(test):
        classCount = [0]*10
        neighbors = []
        for j, x in enumerate(training):
            d = distanceBetweenTwoItems(z,x)
            if len(neighbors) >= k:
                maximum = max(neighbors)
                if maximum[0] > d:
                    neighbors.remove(maximum)
                    neighbors.append([d,j])
                #otherwise the list remains the same and the distance is not added
            else:
                neighbors.append([d,j])
                
        # find the classification based on appearance in the list
        dandc = []
        for n in neighbors:
                d = n[0]
                j = n[1]
                dandc.append([d,classes[j]])
        
        for n in dandc:
            j = int(n[1][0]) # to remove odd 1. formatting
            classCount[j-1] += 1
            # since lists are zero-based, we treat the classification as 0-10 when counting
            # this will be changed back to one-based during the return
        
        maximumCount = 0
        for j,n in enumerate(classCount):
            if classCount[maximumCount] < n:
                maximumCount = j
        classification[i]= maximumCount+1
        
    return classification
    
    # Takes: Two items
    # Returns: Squared euclidean distance between two items
def distanceBetweenTwoItems(x1,x2):
    if (isinstance(x1, (int, float, long)) and isinstance(x2, (int, float, long))): # if the inputs are just single features, compute
        return (x1 -x2)**2 
    if len(x1) == len(x2): #Check that x1 and x2 are of the correct length
        length = 0
        
        for x in range(0,len(x1)):
            length += (x1[x] - x2[x])**2 # the difference between the elements squared
        
        return length
    else:
        return -1 # returns -1 if there's been an issue       
                     
def crossValidation(k):
    i = 1
    while i <= 10:  
        j = 1
        validationSet = data['fold'+str(i)+'_features']
        validationLables = data['fold'+str(i)+'_classes']
        while j<=10:
            if j!=i:
                testSet = data['fold'+str(j)+'_features']
                kNN(k, validationSet,validationLables,testSet)
            j+=1
        i+=1
                
"""
    Runs the cross-validation across the provided data set
    
    Takes: an integer k to perform k Nearest Neighbors
    Result: a printed message describing the incorrect classifications and a
    confusion matrix outlining which classes were confused for others
"""
def crossValidationWithConfusionMatrixA(k):
    missclassification = [[0 for x in xrange(10)] for x in xrange(10)]
    incorrect = 0
    totalClassified = 0
    for x in range(1,11): # for all the possible test folds
        testData  = data['fold'+str(x)+'_features']
        classes = data['fold'+str(x)+'_classes']
        trainingData = []
        trainingClasses = []
        
        for z in range(1,11): # classify given all the training folds
            if x != z:
                t = data['fold'+str(z)+'_features']
                c = data['fold'+str(z)+'_classes']
                trainingData.extend(t)
                trainingClasses.extend(c)
        
        
        classification =  kNN(k,trainingData,trainingClasses,testData)
                    
        for i,c in enumerate(classification):
            trueClass = int(classes[i][0]) -1
            assignedClass = c-1
            if trueClass != assignedClass:
                missclassification[trueClass][assignedClass] += 1 
                incorrect +=1
            totalClassified +=1
    print "Incorrect Classifications: ", incorrect,". Total Classified: ",totalClassified
    return missclassification
    
def crossValidationWithConfusionMatrixB(k):
    missclassification = [[0 for x in xrange(10)] for x in xrange(10)]
    incorrect = 0
    totalClassified = 0
    i = 1
    while i <= 10:
          
        j = 1
        validationSet = data['fold'+str(i)+'_features']
        validationLables = data['fold'+str(i)+'_classes']
        while j<=10:
            if j!=i:
                testSet = data['fold'+str(j)+'_features']
                classification = kNN(k, validationSet,validationLables,testSet)
                numincorrect = 0
                for i,c in enumerate(classification):
                    trueClass = int(validationLables[i][0]) -1
                    assignedClass = c-1
                    if trueClass != assignedClass:
                        missclassification[trueClass][assignedClass] += 1 
                        numincorrect += 1
                        incorrect +=1
                    totalClassified +=1
            j+=1
        print i
        i+=1
       # print "Incorrect Classifications: ", incorrect ,"Total Classifications", totalClassified
    return missclassification