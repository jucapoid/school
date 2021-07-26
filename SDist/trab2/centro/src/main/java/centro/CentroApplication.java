package centro;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CentroApplication {

    private final Properties properties = new Properties();

    private Centro centro;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        CentroApplication centroApplication = null;
        int op;
        Long id;
        System.out.println("1 - centro existente\n2 - registar centro novo\n0 - fechar aplicação\n");
        do {
            op = in.nextInt();
            switch (op) {
                case 0:
                    System.exit(0);
                case 1:
                    System.out.println("Introduza o código do centro:");
                    id = Long.valueOf(in.nextInt());
                    centroApplication = new CentroApplication(id);
                    break;
                case 2:
                    centroApplication = new CentroApplication();
                    int cap = centroApplication.inputInt(2);
                    String cidade = centroApplication.inputStr(1);
                    centroApplication.centro = centroApplication.newCentro(cap, cidade);
                    System.out.println("O código do centro é " + centroApplication.centro.getId());
                    break;
                default:
                    System.out.println("Opção inválida\n");
                    break;
            }
        } while (centroApplication == null);
        centroApplication.menu();
    }

    public CentroApplication() {
        String propertiesPath = Thread.currentThread().getContextClassLoader().getResource("application.properties").getPath();
        try{
            properties.load(new FileInputStream(propertiesPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CentroApplication(Long id) {
        String propertiesPath = Thread.currentThread().getContextClassLoader().getResource("application.properties").getPath();
        try{
            properties.load(new FileInputStream(propertiesPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.centro = getCentro(id);

        System.out.println("Acedido com sucesso à área do centro " + centro.getId() + " na cidade de " + centro.getCidade());
    }

    public void menu() {
        int id;
        String tipo, data;
        try {
            while (true) {
                int op = inputInt(1);
                switch (op) {
                    // Caso 1 para Registar administração de vacina
                    case 1:
                        id = inputInt(3);
                        tipo = inputStr(2);
                        data = inputStr(3);
                        registarVacinaAgendada(Long.valueOf(id), tipo, data);
                        break;
                    // caso 2 para comunicar uma lista de vacinas administradas
                    case 2:
                        saveCentro();
                        break;
                    // caso 3 para receber o numero de vacinas para uma data
                    case 3:
                        centro = getCentro(centro.getId());
                        data = inputStr(3);
                        receberNVacinas(data);
                        break;
                    // caso 0 para fechar programa
                    case 0:
                        System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Centro getCentro(Long id) {
        System.out.println(properties.getProperty("url"));
        String uri = properties.getProperty("url") + "centros/{id}";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Centro> responseEntity = restTemplate.getForEntity(uri, Centro.class, id);
        return responseEntity.getBody();

    }

    Centro newCentro(int cap, String cidade) {
        String uri = properties.getProperty("url") + "centros";

        Centro centro = new Centro(cap, cidade);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(uri, centro, Centro.class);
    }

    void registarVacinaAgendada(Long id, String tipo, String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse(data);
        for (Vacinacao v: centro.getListaMarcacoes()) {
            if (v.getId().equals(id)) {
                v.setTipo(tipo);
                v.setDataAdministracao(date);
                v.setAdministrada(true);
                return;
            }
        }
        System.out.println("Vacinaçao não agendada");
    }

    void receberNVacinas(String data) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String uri = properties.getProperty("url") + "vaxpordia/{date}/{centroId}";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("date", data);
        map.put("centroId", centro.getId().toString());
        int nvax = restTemplate.getForObject(uri, Integer.class, map).intValue();
        Date date = simpleDateFormat.parse(data);
        uri = properties.getProperty("url") + "notificacoes";
        for (Vacinacao v: centro.getListaMarcacoesDiaOrderIdade(date)) {
            System.out.println(nvax);
            if (nvax > 0) {
                Notificacao n = new Notificacao("Vacinação Confirmada", "A vacinação com o código " + v.getId() + " foi confirmada para o dia " + date.toString(), v.getId());
                v.setConfirmada(true);
                v.addNotificacao(n);
                restTemplate.postForObject(uri, n, Void.class);
                nvax--;
            } else {
                Notificacao n = new Notificacao("Vacinação Cancelada", "A vacinação com o código " + v.getId() + " foi cancelada no dia " + date.toString(), v.getId());
                v.addNotificacao(n);
                restTemplate.postForObject(uri, n, Void.class);
            }
        }

        for (Vacinacao v: centro.getListaMarcacoes())
            System.out.println(v.toString());
    }

    void saveCentro() {
        String uri = properties.getProperty("url") + "centros/{id}";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, centro, centro.getId());
    }

    int inputInt(int campo) {
        Scanner in = new Scanner(System.in);
        do {
            switch (campo) {
                case 1 -> System.out.println("\n\n --- MENU --- \n"
                        + "1 - Registar Vacinação já agendada\n"
                        + "2 - Comunicar Lista de Vacinações\n"
                        + "3 - Obter número de vacinas para uma data\n"
                        + "0 - Fechar Aplicação\n");
                case 2 -> System.out.println("Introduza a capacidade diária de vacinas: ");
                case 3 -> System.out.println("Introduza o código de vacinação: ");
                case 4 -> System.out.println("Introduza o número de vacinas na lista");
            }
            try {
                String s = in.nextLine();
                int id = Integer.parseInt(s);
                return id;
            } catch (Exception e) {
                switch (campo) {
                    case 1 -> System.out.println("A operação só pode ser 0, 1, 2 ou 3");
                    case 2 -> System.out.println("A capacidade tem de ser inteiro!");
                    case 3 -> System.out.println("O código de vacinação tem de ser inteiro!");
                    case 4 -> System.out.println("O núero de vacinas tem de ser inteiro!");
                }
            }
        } while (true);
    }

    String inputStr(int campo) {
        Scanner in = new Scanner(System.in);
        String regex = null, s = "";
        do {
            switch (campo) {
                case 1 -> System.out.println("Introduza a cidade: ");
                case 2 -> System.out.println("Introduza o tipo de vacina:");
                case 3 -> {
                    System.out.println("Introduza a data preferida(dd-mm-aaaa):");
                    regex = "[0-3][0-9]-[0-1][0-9]-202[0-9]";
                }
                case 4 -> System.out.println("Introduza o nome:");
            }
            try {
                if (campo == 3) {
                    s = in.next(regex);
                } else {
                    s = in.nextLine();
                }
                return s;
            } catch (Exception e) {
                switch (campo) {
                    case 1 -> System.out.println("Cidade Inválida");
                    case 2 -> System.out.println("Tipo de vacina inválido!");
                    case 3 -> System.out.println("A data tem de ser do formato dd-mm-aaaa!");
                    case 4 -> System.out.println("Nome inválido");
                }
            }
        } while (true);
    }
}
