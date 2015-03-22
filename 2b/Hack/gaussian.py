import numpy
import scipy.io
import math

data = scipy.io.loadmat('C:\\Users\\Alexandr\\Dropbox\\2b\\gtzan.mat')
""" s1210313 """
def run32A_AllFolds():
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
            classification = diagonalCovarianceGaussian(testData[y], trainingData, trainingClasses)
            actualClass = int(testClasses[y][0])
            
            if classification != int(testClasses[y][0]):
                totalMissclassified += 1
                confusionMatrix[actualClass-1][classification-1]+=1
            totalClassified += 1
            
    print "Total Misclassified: ",totalMissclassified ," Total Classifications: ", totalClassified 
    print confusionMatrix 
            
def diagonalCovarianceGaussian(testData, trainingData, trainingClasses):
    maxProb = 0
    bestClass = 0
    for i in range(1,11): #for all the classes
        matrix = []
        for j,x in enumerate(trainingClasses):
            if x == i: # if the item is in the class we're classifying for
                matrix.append(trainingData[j]) # add it to the class matrix
                
        sigma =0L+ numpy.cov(matrix,rowvar=0)#full covariance matrix over all classes
        
        sigmaPrime = [[0 for x in xrange(len(sigma))] for x in xrange(len(sigma))]
        for j in range(0, len(sigma)):
            sigmaPrime[j][j] = sigma[j][j]
            j += 1
        
        sigma1 = numpy.linalg.inv(sigmaPrime)
        
        sigmaDet = 0L + numpy.linalg.det(sigmaPrime)
        
        mu = numpy.mean(matrix, axis =0)# take the new matrix and form mu and sigma
          
        term = numpy.subtract(testData, mu)
        termT = numpy.transpose(term)
        
        p1 = (-0.5)*(numpy.dot(numpy.dot(termT,sigma1),term))
        p2 = (0.5)*(numpy.log(sigmaDet))
        probability = p1-p2
        
        if probability > maxProb or maxProb == 0:
            maxProb = probability
            bestClass = i
    return bestClass



def run32B_AllFolds():
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
            classification = equalCovarianceGaussian(testData[y], trainingData, trainingClasses)
            actualClass = int(testClasses[y][0])
            
            if classification != int(testClasses[y][0]):
                totalMissclassified += 1
                confusionMatrix[actualClass-1][classification-1]+=1
            totalClassified += 1
            
    print "Total Misclassified: ",totalMissclassified ," Total Classifications: ", totalClassified 
    print confusionMatrix 
            
def equalCovarianceGaussian(testData, trainingData, trainingClasses):
    maxProb = 0
    bestClass = 0
    for i in range(1,11): #for all the classes
        matrix = []
        for j,x in enumerate(trainingClasses):
            if x == i: # if the item is in the class we're classifying for
                matrix.append(trainingData[j]) # add it to the class matrix
                
        sigma =0L+ numpy.cov(trainingData,rowvar=0)#full covariance matrix over all classes
        sigma1 = numpy.linalg.inv(sigma)
          
        mu = numpy.mean(matrix, axis =0)# take the new matrix and form mu and sigma
          
        term = numpy.subtract(testData, mu)
        termT = numpy.transpose(term)
        
        probability = (-0.5)*(numpy.dot(numpy.dot(termT,sigma1),term))
        if probability > maxProb or maxProb == 0:
            maxProb = probability
            bestClass = i
    return bestClass
    
    
    
    
    
    
    