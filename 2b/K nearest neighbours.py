import scipy.io
from numpy import matrix
from numpy import linalg


data = scipy.io.loadmat('C:\\Users\\Alexandr\\Dropbox\\2b\\gtzan.mat')

""" 
Cross Validation Setup

Lecture notes: http://www.inf.ed.ac.uk/teaching/courses/inf2b/learnSlides/inf2b14-learnlec15-full.pdf

K-fold Cross-validation: divide the data into k partitions.
Itterate across these partitions, selecting them one by one
to be the validation set, whilst the rest are the training
sets.

To get an element from a dictionary:
dic['key']

"""

def main():
    #train = data['fold1_features']
    #test = data['fold2_features']
    #classes = data['fold1_classes']
    train = [1,2,3]
    test = [1,2,3]
    classes = [100,99,98]

    kNN(1,train,classes,test)
    # try classifying for k = 1,3,5

def crossValidation():
    for x in range(1,10):
        validationSet = data['fold'+x+'_features']
        
    # Takes: training set and testing set
    # Returns c(z)
    
def kNN(k, training, classes, test):
    """Need to test & need to tiebreak if two classes equal"""
    # for each example in test set
    for i, z in enumerate(test):
        # and each element in Training
        classCount = [10]
        neighbors = [] # creates an array to store the distances of each and class.
        for index, x in enumerate(training):
            # compute the distance between the two
            d = distanceBetweenTwoItems(z,x)
            if len(neighbors) > k:
                maximum = max(neighbors)
                print 
                if d < maximum:
                    neighbors.pop(maximum)
                    neighbors.append([d,index])
                neighbors.append([d,index])
            for n in neighbors:
                # count the class frequency for this class
                for c in n[1]:
                    classCount[c] += 1
            
            test[i] = [z,c] # each array is a two dimensional 
    return test        
     
       
    # Takes: Two items
    # Returns: Squared euclidean distance between two items
def distanceBetweenTwoItems(x1,x2):
    if (isinstance(x1, (int, float, long)) and isinstance(x2, (int, float, long))):
        return (x1 -x2)**2 
    if len(x1) == len(x2): #Check that x1 and x2 are of the correct length
        length = 0
        
        for x in range(0,len(x1)):
            length += (x1[x] - x2[x])**2 # the difference between the elements squared
        
        return length
    else:
        return -1 # returns -1 if there's been an issue 
