
//Função de Hash djb2 retirada de http://www.cse.yorku.ca/~oz/hash.html 
unsigned long hash(char* id_stu)
{
	unsigned long hash = 5381;
	int c;

	while ((c = *id_stu++))
		hash = ((hash << 5) + hash) + c; /* hash * 33 + c */

	return hash;
}
