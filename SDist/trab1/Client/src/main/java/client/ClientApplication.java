package client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientApplication {

    private final Properties properties = new Properties();

    private final RestTemplate rt = new RestTemplate();

    ClientApplication() {
        String propertiesPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.properties")).getPath();
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.menu();
    }

    public void menu() {
        long id;
        try {
            while (true) {
                int op = intInput("menu");
                switch (op) {
                    case 0 -> System.exit(0);
                    case 1 -> consultaCentros();
                    case 2 -> {
                        id = intInput("centroId");
                        filaEspera(id);
                    }
                    case 3 -> {
                        id = intInput("centroId");
                        String nome = strInput("nome");
                        char gen = strInput("genero").charAt(0);
                        int idade = intInput("idade");
                        inscrever(id, nome, gen, idade);
                    }
                    case 4 -> {
                        id = intInput("vaxId");
                        String data = strInput("data");
                        String tipo = strInput("tipo");
                        realizar(id, data, tipo);
                    }
                    case 5 -> {
                        id = intInput("vaxId");
                        efeitos(id);
                    }
                    case 6 ->  administradasPorTipo();
                    case 7 ->  efeitosPorTipo();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultaCentros() {
        String uri = properties.getProperty("url") + "centros";
        ResponseEntity<Centro[]> responseEntity = rt.getForEntity(uri, Centro[].class);
        Centro[] clist = responseEntity.getBody();
        if (clist != null && clist.length > 0) {
            for (Centro c : clist) {
                System.out.println(c.toString());
            }
        } else {
            System.out.println("\nNão existem centros de momento");
        }
    }

    public void filaEspera(Long id) {
        String uri = properties.getProperty("url") + "centros/{id}";
        ResponseEntity<Centro> result = rt.getForEntity(uri, Centro.class, id);
        Centro c = result.getBody();
        if (c != null) {
            System.out.println("\nA fila de espera do centro " + c.getId() + " tem " + c.countFila() + " pessoas");
        } else {
            System.out.println("\nCentro " + id + " não encontrado");
        }

    }

    public void inscrever(Long id, String nome, char gen, int idade){
        String uri = properties.getProperty("url") + "vacinacoes";
        Vacinacao v = new Vacinacao(nome, idade, gen, id);
        v = rt.postForObject(uri, v, Vacinacao.class);
        System.out.println("\nO id de vacinação é " + v.getId());
    }

    public void realizar(Long id, String data, String tipo) throws ParseException {
        String uri = properties.getProperty("url") + "vacinacoes/{id}";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
        Date date = sdf.parse(data);
        ResponseEntity<Vacinacao> result = rt.getForEntity(uri, Vacinacao.class, id);
        Vacinacao v = result.getBody();
        if (v != null) {
            if (!v.isAdministrada()) {
                v.setAdministrada(true);
                v.setDataAdministracao(date);
                v.setTipo(tipo);
                rt.put(uri, v, v.getId());
            } else {
                System.out.println("\nVacinação com id " + v.getId() + " já administrada");
            }
        } else {
            System.out.println("\nNão existe marcação com o id " + id);
        }
    }

    public void efeitos(Long id) {
        String uri = properties.getProperty("url") + "vacinacoes/{id}";
        ResponseEntity<Vacinacao> result = rt.getForEntity(uri, Vacinacao.class, id);
        Vacinacao v = result.getBody();
        if (v != null) {
            if (v.isAdministrada()) {
                v.setEfeitos(true);
                rt.put(uri, v, v.getId());
            } else {
                System.out.println("\nEsta vacina ainda não foi administrada");
            }
        } else {
            System.out.println("\nVacinação " + id + " inexistente");

        }
    }

    public void administradasPorTipo() {
        String uri = properties.getProperty("url") + "vacinacoes";
        ResponseEntity<Vacinacao[]> result = rt.getForEntity(uri, Vacinacao[].class);
        Vacinacao[] vlist = result.getBody();
        if (vlist != null && vlist.length > 0) {
            List<String> tipos = new ArrayList<>();
            for (Vacinacao v: vlist) {
                if (v.isAdministrada() && !tipos.contains(v.getTipo()))
                    tipos.add(v.getTipo());
            }
            if (tipos.size() > 0) {
                for (String tipo : tipos) {
                    int n = 0;
                    for (Vacinacao v : vlist) {
                        if (v.isAdministrada() && tipo.equals(v.getTipo()))
                            n++;
                    }
                    System.out.println("\nForam administradas " + n + " vacinas " + tipo);
                }
            } else {
                System.out.println("\nNão foram administradas vacinas até ao momento");
            }
        } else {
            System.out.println("\nNão existem vacinações guardadas");
        }
    }

    public void efeitosPorTipo() {
        String uri = properties.getProperty("url") + "vacinacoes";
        ResponseEntity<Vacinacao[]> result = rt.getForEntity(uri, Vacinacao[].class);
        Vacinacao[] vlist = result.getBody();
        if (vlist != null && vlist.length > 0) {
            List<String> tipos = new ArrayList<>();
            for (Vacinacao v: vlist) {
                if (v.isEfeitos() && !tipos.contains(v.getTipo()))
                    tipos.add(v.getTipo());
            }
            if (tipos.size() > 0) {
                for (String tipo : tipos) {
                    int n = 0;
                    for (Vacinacao v : vlist) {
                        if (v.isEfeitos() && tipo.equals(v.getTipo()))
                            n++;
                    }
                    System.out.println("\nForam registados " + n + " casos de efeitos secundários nas vacinas " + tipo);
                }
            } else {
                System.out.println("\nNão foram registados efeitos secundários até agora");
            }
        } else {
            System.out.println("\nNão existem vacinações guardadas");
        }}

    public int intInput(String campo) {
        Scanner in = new Scanner(System.in);
        int res;
        do {
            switch (campo) {
                case "menu" -> System.out.println("\n\n ---- MENU ---- \n" +
                        "1 - Consultar centros de vacinação\n" +
                        "2 - Comprimento fila de espera\n" +
                        "3 - Inscrever para vacinação\n" +
                        "4 - Registar realização de vacina\n" +
                        "5 - Reportar efeitos secundários\n" +
                        "6 - Listar total de vacinados por tipo de vacina\n" +
                        "7 - Listar total de efeitos secundários por tipo de vacina\n" +
                        "0 - Fechar Aplicação\n");
                case "centroId" -> System.out.println("Introduza o ID do centro: ");
                case "idade" -> System.out.println("Idade: ");
                case "vaxId" -> System.out.println("Id da inscrição: ");
            }
            try {
                res = in.nextInt();
                return res;
            } catch (Exception e) {
                switch (campo) {
                    case "menu" -> System.out.println("\nEscolha uma opção válida");
                    case "centroId", "vaxId" -> System.out.println("\nO ID deve ser um inteiro");
                    case "idade" -> System.out.println("\nA idade deve ser inteira");
                }
            }
        } while (true);
    }

    public String strInput(String campo) {
        Scanner in = new Scanner(System.in);
        String s, regex;
        do {
            switch (campo) {
                case "nome" -> System.out.println("Nome: ");
                case "genero" -> System.out.println("Género(M/F): ");
                case "data" -> System.out.println("Data vacinação(DD-MM-AAAA): ");
                case "tipo" -> System.out.println("Tipo de vacina: ");
            }
            try {
                switch (campo){
                    case "genero" -> {
                        regex = "[M|F]";
                        s = in.next(regex);
                    }
                    case "data" -> {
                        regex = "[0-3][0-9]-[0-1][0-9]-202[0-9]";
                        s = in.next(regex);
                    }
                    default -> s = in.nextLine();
                }
                return s;
            } catch (Exception e) {
                switch (campo) {
                    case "nome"  -> System.out.println("\nNome inválido");
                    case "genero"  -> System.out.println("\nO género tem de ser M ou F");
                    case "data" -> System.out.println("\nO formato da data tem de ser DD-MM-AAAA");
                    case "tipo" -> System.out.println("\nTipo inválido");
                }
            }
        } while (true);
    }
}