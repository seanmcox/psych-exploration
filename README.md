# psych-exploration
This project is a little different from most in that that point is not any sort of application, and none of the main code entry points are intended to be particularly useful as anything but an example. 

The intent of this code is probably closest to that of a library. My intent is to develop code that can be used to tease out the underlying connections between parameters in a data set. To that end, my first goal is to develop code for high-dimensional principal component analysis suitable for analyzing survey results. This is what I am working on right now. Currently I have gotten the determinant calculation to be fast enough for my needs, but I sometimes run into some difficult conditions when trying to calculate the eigenvectors of the matrix. The next step will be to develop code for factor analysis. Finally, I hope to develop some code that utilizes machine learning to explore less linear relationships between factors.

I have been using a free HEXACO data set available online from https://openpsychometrics.org/_rawdata/

