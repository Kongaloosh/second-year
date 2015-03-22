import scipy.io
import math
"""S1210313"""

data = scipy.io.loadmat('gtzan.mat')

def runknn(k):
    totalClassified = 0
    totalMissclassified = 0
    confusionMatrix = [[0 for x in xrange(10)] for x in xrange(10)]
    
    for x in range(1,11): # try every fold
        testData  = data['fold'+str(x)+'_features']
        testClasses = data['fold'+str(x)+'_classes']
        
        trainingData = []
        trainingClasses = []        
        
        for y in range(1,11): #for all other folds
            if y!=x: # if this fold isn't testing data
            
            #train on it
                t = data['fold'+str(y)+'_features']
                c = data['fold'+str(y)+'_classes']
                trainingData.extend(t)
                trainingClasses.extend(c)
        
        # for all the pieces of test data
        for y in range(len(testData)): 
            #classify
            classification = knn(k, testData[y], trainingData, trainingClasses)
            actualClass = int(testClasses[y][0])
            
            if classification != int(testClasses[y][0]):
                totalMissclassified += 1
                confusionMatrix[actualClass-1][classification-1]+=1
            totalClassified += 1
            
    print "Total Misclassified: ",totalMissclassified ," Total Classifications: ", totalClassified 
    print confusionMatrix 
    
    
def knn(k, testData, trainingData, trainingClasses):
    neighbors=[]
    
    for i,x in enumerate(trainingData):
        d = distanceBetweenTwoItems(testData,x)
        if len(neighbors) >=k:
            maximum = max(neighbors)
            if maximum[0] > d:
                neighbors.remove(maximum)
                neighbors.append([d,i])
        else:
            neighbors.append([d,i])
    
    for i,n in enumerate(neighbors):  
        index = n[1]
        neighbors[i][1] = int(trainingClasses[index][0])
    return classify(neighbors)

def classify(neighbors):
    classCount = [0]*10
    for i,n in enumerate(neighbors):
        classCount[n[1]-1] += 1
    
    maxClass = max(classCount)
    classret = -1
    for i, count in enumerate(classCount):
        if count == maxClass:
            if classret == -1:
                classret = i+1
            else:
                newneighbors = []
                maximum = max(neighbors)
                for n in neighbors:
                    if n != maximum:
                        newneighbors.append(n)
                classify(newneighbors)
    return classret
                    
                
            
            
            
        
    # if another point has the same value, pop it
        
        
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
                       