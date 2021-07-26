package cidadao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CidadaoApplication {

    private Properties properties = new Properties();

    private Vacinacao vacinacao;

    public static void main(String[] args) {
        CidadaoApplication cidadaoApplication = new CidadaoApplication();
        cidadaoApplication.menu();
    }

    public CidadaoApplication() {
        String propertiesPath = Thread.currentThread().getContextClassLoader().getResource("application.properties").getPath();
        try{
            properties.load(new FileInputStream(propertiesPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menu() {
        long id;
        int age;
        String name;
        String email;
        String data;
        try {
            while (true) {
                int op = inputInt(1);
                switch (op) {
                    case 0:
                        System.exit(0);
                    case 1:
                        for (Centro c: consultaCentros()) {
                            System.out.println(c.toString() + "\n");
                        }
                        break;
                    case 2:
                        id = inputInt(2);
                        name = inputStr(1);
                        age = inputInt(3);
                        email = inputStr(2);
                        data = inputStr(3);
                        vacinacao = autoagendar(id, name, age, email, data);
                        System.out.println("O seu codigo de vacinação é " + vacinacao.getId());
                        break;
                    case 3:
                        id = inputInt(4);
                        vacinacao = getVacinacao(id);
                        List<Notificacao> result = new ArrayList<>();
                        if (vacinacao == null) {
                            System.out.println("Não existe marcação com código " + op);
                        } else {
                            if (vacinacao.isUnread()){
                                for (Notificacao n: vacinacao.unread()) {
                                    n.setLida(true);
                                    saveNotificacao(n);
                                    result.add(n);
                                }
                                for (Notificacao n: result)
                                    System.out.println(n.toString());
                            } else {
                                System.out.println("\nNão tem notificações por ler");
                            }
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveNotificacao(Notificacao notificacao) {
        String uri = properties.getProperty("url") + "notificacoes/{id}";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, notificacao, notificacao.getId());
    }

    Vacinacao getVacinacao(Long id) {
        String uri = properties.getProperty("url") + "vacinacoes/{id}";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Vacinacao> responseEntity = restTemplate.getForEntity(uri, Vacinacao.class, id);

        return responseEntity.getBody();
    }

    Centro[] consultaCentros() {
        String uri = properties.getProperty("url") + "centros";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Centro[]> result = restTemplate.getForEntity(uri, Centro[].class);

        return result.getBody();
    }

    Vacinacao autoagendar(Long centroId, String nome, int idade, String email, String data) throws ParseException {

        String uri = properties.getProperty("url") + "vacinacoes";
        RestTemplate restTemplate = new RestTemplate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse(data);
        Vacinacao v = new Vacinacao(nome, email, idade, date, centroId);

        return restTemplate.postForObject(uri, v, Vacinacao.class);
    }

    int inputInt(int campo) {
        Scanner in = new Scanner(System.in);
        do {
            switch (campo) {
                case 1:
                    System.out.println("\n\n --- MENU --- \n"
                            + "1 - Consultar Centros de Vacinação\n"
                            + "2 - Autoagendar Vacinação\n"
                            + "3 - Verificar Notificações para Marcação existente\n"
                            + "0 - Fechar Aplicação\n");
                    break;
                case 2:
                    System.out.println("Introduza o id do centro: ");
                    break;
                case 3:
                    System.out.println("Introduza a sua idade: ");
                    break;
                case 4:
                    System.out.println("Introduza o código da sua marcação");
                    break;
            }
            try {
                String s = in.nextLine();
                int id = Integer.parseInt(s);
                return id;
            } catch (Exception e) {
                switch (campo) {
                    case 1:
                        System.out.println("A operação só pode ser 0, 1, 2 ou 3");
                        break;
                    case 2:
                        System.out.println("O id tem de ser inteiro!");
                        break;
                    case 3:
                        System.out.println("A idade tem de ser inteiro!");
                        break;
                    case 4:
                        System.out.println("O código tem de ser inteiro!");
                }
            }
        } while (true);
    }

    String inputStr(int campo) {
        Scanner in = new Scanner(System.in);
        String regex = null, s = "";
        do {
            switch (campo) {
                case 1:
                    System.out.println("Introduza o seu nome: ");
                    break;
                case 2:
                    System.out.println("Introduza o seu email:");
                    break;
                case 3:
                    System.out.println("Introduza a data preferida(dd-mm-aaaa):");
                    break;
                case 4:
                    System.out.println("Indique o seu sexo(M/F)");
                    break;
            }
            try {
                switch (campo) {
                    case 1 -> s = in.nextLine();
                    case 2 -> {
                        regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                        s = in.next(regex);
                    }
                    case 3 -> {
                        regex = "[0-3][0-9]-[0-1][0-9]-202[0-9]";
                        s = in.next(regex);
                    }
                    case 4 -> {
                        regex = "[M|F]";
                        s = in.next(regex);
                    }
                }
                return s;
            } catch (Exception e) {
                switch (campo) {
                    case 1 -> System.out.println("Nome não válido");
                    case 2 -> System.out.println("O email introduzido não é válido!");
                    case 3 -> System.out.println("A data tem de ser do formato dd-mm-aaaa!");
                    case 4 -> System.out.println("O sexo tem de ser M ou F!");
                }
            }
        } while (true);
    }
}
