%myPermutation - função que devolve combinações de tamanho N com carateres presentes em Set
%Retirada de https://stackoverflow.com/questions/43102965/prolog-how-to-create-all-possible-permutations-with-repetition-give-a-list-of-o
eval([],_).
eval([H|T],Set):-member(H,Set),eval(T,Set).

myPermutation(N, Set, L):-length(L,N), eval(L,Set).


%del - função que elimina uma lista de outra, mas só no caso de a primeira ser prefixo da segunda
del([],M,M).
del([H|T], [H|T1], M) :- del(T, T1, M).

%check - função que constrói uma possível leitura da palavra M com o alfabeto separado em letras já testadas, letra atual, letras seguintes. devolve em L a palavra descodificada
check(_, _, _, [], []) :- !.
check([], (K,V), A, M, L) :- 
	del(V, M, M1),
	check([], (K,V), A, M1, L1),
	append([K], L1, L).

check([(KB,VB)|TB], (K,V), A, M, L) :- 
	del(V, M, M1),
	append(TB, [(K,V)], A1), 
	append(A1, A, A2),
	check([], (KB,VB), A2, M1, L1),
	append([K], L1, L).

check(B, (K,V), [(KA,VA)|TA], M, L) :- 
	append(B, [(K,V)], NB),
	check(NB, (KA,VA), TA, M, L).

%checkperms - função que recebe o alfabeto, uma lista de combinações de 0 e 1 e devolve uma lista com o código ambiguo e duas possíveis leituras
checkperms(_, [], _) :- false, !.
checkperms([HA|TA], [H|_], L) :- findall(L1, check([], HA, TA, H, L1), [H1L2,H2L2|_]), append([H],[H2L2,H1L2],L), !. 
checkperms(A, [_|T], L) :- checkperms(A, T, L). 

%start - função que envia uma lista de combinações para o checkperms, se não houver nenhum código ambiguo nessa lista tenta todas as combinações com mais um caracter de comprimento
start(N, A, R) :- myPermutation(N, [0,1], L), checkperms(A, [L], R), !.
start(N, A, R) :- N1 is N+1, start(N1, A, R).

ambiguo(A, M, T1, T2) :- start(1, A, [M, T1, T2]).
