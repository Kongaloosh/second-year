import numpy
import scipy.io
import math

"""
Assume a uniform prior distribution over classes. What is the prior probability for each class? What
classifcation accuracy would we obtain if we classifed each music fragment by choosing a label at random?
"""

data = scipy.io.loadmat('C:\\Users\\Alexandr\\Dropbox\\2b\\gtzan.mat')

def run31A():
    totalClassification = 0
    incorrectClassification = 0
    confusionMatrix = [[0 for x in xrange(10)] for x in xrange(10)]
    
    for x in range(1,10): # for all the possible test folds
        testData  = data['fold'+str(x)+'_features']
        classes = data['fold'+str(x)+'_classes']
        classificationCount = [0]*10 # keeps track of the classification tally for each fold
        for y in range(0,len(testData)): # for all the entries in the test folds
            for z in range(1,10): # classify given all the training folds
                if x != z:
                    trainingData = data['fold'+str(z)+'_features']
                    trainingClasses = data['fold'+str(z)+'_classes']
                    classification = multivariateClassification(trainingClasses, trainingData, testData[y])
                    classificationCount[(classification-1)]+=1 # increment the classification for th
            max = 0
            classification = 0
            for i,x in enumerate(classificationCount):
                if x>max:
                    max = x
                    classification = i+1
                    
            actualClass = int(classes[y][0])            
            if classification != actualClass:
                incorrectClassification += 1
                confusionMatrix[actualClass-1][classification-1] +=1
            totalClassification += 1
                        

    print "confusion matrix: ", confusionMatrix
    print "incorrect: ", incorrectClassification, "total", totalClassification 
 
"""
REGULARLIZATION BY ADDING A SMALL CONSTANT TO THE DIAGONAL

Fit a Gaussian model to each music genre, all of them sharing the same full covariance matrix (remember to
substract the mean of its class to each datapoint before calculating the shared covariance matrix). Obtain
a linear discriminant function from these models. Classify the test dataset using the linear discriminant
function. What is the classication accuracy? What is the shape of the classication boundaries of this
method? (There is no need to run experiments to answer this last question)

"""                                            
#
#                                                                    
def gaussianFullCov(trainingLables, trainingData, testData):
    i = 0
    maxProbability = 0
    mostProbableClass = 0    
    while i<10: # For all the classes
        matrix = [] # generate a matrix
        for j,x in enumerate(trainingLables):
            if (int(x[0])) == i+1:
                matrix.append(trainingData[j])
        
        sigma = numpy.cov(testData,rowvar=0)
        d = len(sigma) 
        mu = numpy.mean(matrix, axis =0)# take the new matrix and form mu and sigma
            
        sigmaDet = 0L + numpy.linalg.det(sigma)
        """Note: given the posterior probability is assumed to be the same for
            all classes, and the constant is the same for all classes we can
            remove it from the calculation of probabilty. 
        """   
        firstTerm = 0L + -0.5* numpy.dot(numpy.dot((numpy.transpose(trainingData[0] - mu)), (sigma**-1)),(trainingData[0]-mu))
        secondTerm = 0L + -0.5 * math.log(sigmaDet)
        probability = firstTerm - secondTerm

        if probability > maxProbability:
            maxProbability = probability
            mostProbableClass = i+1
        i+=1
    return mostProbableClass


"""
Fit a diagonal covariance Gaussian model to each genre. What is the classifcation accuracy?. Are the
classifcation boundaries of this method linear? (There is no need to run experiments to answer this last
question)

P(x|mu,sigma) = 1/((2pi)^(d/2)*det(sigma)^(1/2)) exp (-1/2(x-mu)Sigma^-1(x-mu))
C:\Users\Alexandra
det e is the determinant of the matrix
"""

# takes a training fold and a test vector
# returns a classification of the testVector
def multivariateClassification(trainingLables, trainingData, testData):
    i = 0
    maxProbability = 0
    mostProbableClass = 0    
    while i<10: # For all the classes
        matrix = [] # generate a matrix
        for j,x in enumerate(trainingLables):
            if (int(x[0])) == i+1:
                matrix.append(trainingData[j])
        
        sigma = numpy.cov(matrix,rowvar=0)
        d = len(sigma) 
        mu = numpy.mean(matrix, axis =0)# take the new matrix and form mu and sigma
        
        sigmaPrime = [[0 for x in xrange(len(sigma))] for x in xrange(len(sigma))]
        #diagonailze sigma 
        
        
        for j in range(0, len(sigma)):
            sigmaPrime[j][j] = sigma[j][j]
            j += 1
            
        sigmaDet = 0L + numpy.linalg.det(sigmaPrime)
        """Note: given the posterior probability is assumed to be the same for
            all classes, and the constant is the same for all classes we can
            remove it from the calculation of probabilty. 
        """   
        firstTerm = 0L + -0.5* numpy.dot(numpy.dot((numpy.transpose(trainingData[0] - mu)), (sigma**-1)),(trainingData[0]-mu))
        secondTerm = 0L + -0.5 * math.log(sigmaDet)
        probability = firstTerm - secondTerm

        if probability > maxProbability:
            maxProbability = probability
            mostProbableClass = i+1
        i+=1
    return mostProbableClass
          
   

