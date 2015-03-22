"""s1210313

Note: you may have to change the directory description for gtzan.mat for
the computer you are using. This can be found in knn.py and gaussian.py 
labelled as 'data'

All questions to questions posed in section 3.1 and 3.2 can be found in the report

"""

import gaussian
# reports the confusion matrix and accuracy of 3.1, or diagonalized gaussian
gaussian.run32A_AllFolds()


# reports the confusion matrix and accuracy of 3.1, or diagonalized gaussian
gaussian.run32B_AllFolds()


import knn
# reports the confusion matrix and accuracy of 2, or KNN for value k
k = 1
knn.runknn(k)


