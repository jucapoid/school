import numpy as np
from sklearn.model_selection import train_test_split
import math

class DecisionTree:

    def __init__(self, criterion='gini', prune=True):
        self.x_train = np.array
        self.y_train = np.array
        self.atributes = list
        self.num_atributes = int
        self.train_size = int

    def _frequency_table(self, col, ytrain):
        '''
        Recebe uma coluna do xtrain (atributo) e a coluna do ytrain
        Retorna a matriz de frequencias desse atributo
        '''
        unix, uniy = np.unique(col), np.unique(ytrain)
        assort = np.zeros((np.size(unix), np.size(uniy)))
        
        for i in range(len(unix)):
            for k in range(len(uniy)):
                assort[i][k]=len(np.where((col==unix[i])*(ytrain==uniy[k]))[0])

        return assort

    def _entropy(self, x):
        '''
        Recebe uma coluna do xtrain
        Retorna a entropia dessa coluna
        '''
        s = np.sum(x)
        t = 0
        for i in x:
            if i > 0:
                t += -(i/s)*math.log2(i/s) 
        return t

    def _gini(self, x):
        '''
        Recebe uma coluna do xtrain
        Retorna o indice de gini dessa coluna
        '''
        s = np.sum(x)
        t = 0
        for i in x:
            t += (i/s)**2
        return 1 - t

    def _attribute_entropy(self, col, ytrain):
        '''
        Recebe uma coluna do xtrain (atributo) e a coluna do ytrain
        Retorna a impureza calculada por entropia desse atributo
        '''
        total = 0
        freq = self._frequency_table(col, ytrain)

        for j in range(len(freq)):
            t = np.sum(freq[j])
            entropia = self._entropy(freq[j])
            total+=t/len(col)*entropia

        return total
    
    def _attribute_gini(self, col, ytrain):
        '''
        Recebe uma coluna do xtrain (atributo) e a coluna do ytrain
        Retorna a impureza calculada por gini desse atributo
        '''
        total = 0
        freq = self._frequency_table(col, ytrain)

        for j in range(len(freq)):
            t = np.sum(freq[j])
            gini = self._gini(freq[j])
            total += t/len(col)*gini

        return total

    def _info_gain(self, x, y):
        '''
        Recebe uma coluna do xtrain e a coluna ytrain
        Retorna o information gain
        '''
        _, freqy = np.unique(y, return_counts=True)
        y_entropy = self._entropy(freq)
        return y_entropy - self._attribute_entropy(x, y)

    def _best_split(self, x, y, crit="gini"):
        '''
        Recebe o conjunto completo do xtrain e do ytrain e ainda o descritor de que indice de pureza utilizar
        Retorna o indice da coluna com maior pureza
        '''
        col = 0
        m = 1

        for i in range(len(x[0])):
            if crit == "gini":
                t = self._attribute_gini(x[:,i], y)
            elif crit == "entropy":
                t = self._attribute_entropy(x[:,i], y)
            if t < m:
                m = t
                col = i
        return col

    def _build_tree(self, train):
        ''' Método recursivo para construção da árvore
        Recebe todo o dataset (x_train + y_train)
        '''
        #encontrar a coluna com menor impureza e ordenar o dataset por essa coluna
        col = self._best_split(train[:,:-1],train[:,-1])        
        train = train[train[:,col].argsort()]

        for val in np.unique(train[:,col]):
            #criar um novo dataset menor só com os valores da coluna escolhida iguais
            new_train = train[np.where(train[:,col]==val)]
            print()
            print(new_train)
            #recursivamente chamar este método só no caso do dataset não ser folha
            if len(new_train) != 1 and not np.all(new_train[:,-1] == new_train[0,-1]):
                self._build_tree(new_train)


    def fit(self, x, y):
        #Guardar uma lista com o nome dos atributos/features
        self.atributes = x[0,:]
        
        self.x_train, _, self.y_train, _ = train_test_split(x[1:,:], y[1:], random_state=0)

        #variável para o numero de atributos
        self.num_atributes = self.x_train.shape[1]
        #variavel para tamanho do dataset
        self.train_size = self.x_train.shape[0]
        
        #alterar o ytrain para ser concatenado como coluna à frente do xtrain
        new_y = self.y_train.reshape(-1,1)
        dataset = np.append(self.x_train, new_y, axis=1)
        self._build_tree(dataset)

#Exemplo dos slides#################################################################
data1=np.genfromtxt("teste.csv", delimiter=",", dtype=None, encoding=None)
xdata1=data1[:,0:-1]    #  dados: da segunda à ultima linha, da primeira à penúltima coluna  
ydata1=data1[:,-1]      # classe: da segunda à ultima linha, só última coluna
########################################################################################

classifier = DecisionTree()
classifier.fit(xdata1, ydata1)
#result = classifier.score(x_test, y_test)
#print("Percentagem de casos corretamente classificados {:2.2%}".format(result))
