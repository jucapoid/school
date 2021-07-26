package dgs;

import dgs.exceptions.CentroNotFoundException;
import dgs.models.*;
import dgs.repos.CentroRepo;
import dgs.repos.VacinacaoRepo;
import dgs.repos.VaxPorDiaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class DGSCommandLine implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DGSApplication.class);

    @Autowired
    VaxPorDiaRepo vaxPorDiaRepo;

    @Autowired
    CentroRepo centroRepo;

    @Autowired
    VacinacaoRepo vacinacaoRepo;

    @Override
    public void run(String... args) throws Exception {
        String data;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        while (true) {
            int op = inputInt("menu");
            switch (op) {
                case 0 -> System.exit(0);
                case 1 -> {
                    data = inputStr("data");
                    date = format.parse(data);
                    int n = inputInt("nvacinas");
                    distribuirVacinas(date, n);
                }
                case 2 -> {
                    listaPorDia();
                }
                case 3 -> {
                    for (Centro c : centroRepo.findAll()) {
                        System.out.println(c.toString() + "\n");
                    }
                }
                case 4 -> {
                    for (Vacinacao v : vacinacaoRepo.findAll()) {
                        System.out.println(v.toString());
                    }
                }
            }
        }
    }

    void distribuirVacinas(Date data, int n) {
        int nvax;
        if (vaxPorDiaRepo.findByData(data) != null) {
            System.out.println("As vacinas desse dia já se encontram atribuidas");
        } else {
            List<Vacinacao> agendadas = vacinacaoRepo.findByDataPreferidaAndAdministradaOrderByIdade(data, false);
            List<VaxPorDia> vaxPorDias = new ArrayList<>();
            for (Centro c : centroRepo.findAll()) {
                vaxPorDias.add(new VaxPorDia(c.getId(), data, 0));
            }
            for (VaxPorDia v : vaxPorDias) {
                for (int i = 0; i < n; i++) {
                    Centro c = centroRepo.findById(v.getCentroId()).orElseThrow(() -> new CentroNotFoundException(v.getCentroId()));
                    if (v.getNumVacinas() >= c.getCapacidade())
                        n++;
                    else if (v.getCentroId() == agendadas.get(i).getCentroId())
                        v.incrementNumVacinas();
                }
            }
            vaxPorDiaRepo.saveAll(vaxPorDias);
        }
    }

    void listaPorDia() {
        List<Date> dias = new ArrayList<>();
        List<String> tipos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (Vacinacao v: vacinacaoRepo.findAll()) {
            if (v.isAdministrada() && !tipos.contains(v.getTipo()))
                tipos.add(v.getTipo());
            if (v.isAdministrada() && !dias.contains(v.getDataAdministracao()) )
                dias.add(v.getDataAdministracao());
        }

        for (Date d: dias) {
            System.out.println(sdf.format(d));
            for (String t: tipos) {
                int n = vacinacaoRepo.findByDataAdministracaoAndTipo(d, t).size();
                System.out.println("|   " + n + " " + t);
            }

        }
    }

    int inputInt(String campo) {
        Scanner in = new Scanner(System.in);
        do {
            switch (campo) {
                case "menu" ->
                    System.out.println("\n\n --- MENU --- \n"
                            + "1 - Inserir nº de vacinas para uma data\n"
                            + "2 - Listar Vacinação por tipo e por dia\n"
                            + "3 - Listar Centros\n"
                            + "4 - Listar Marcações\n"
                            + "0 - Fechar Aplicação\n");
                case "nvacinas" ->
                    System.out.println("Introduza o número de vacinas a nivel nacional para essa data");
                case "3" -> {}
            }
            try {
                String s = in.nextLine();
                int id = Integer.parseInt(s);
                return id;
            } catch (Exception e) {
                switch (campo) {
                    case "menu" ->
                        System.out.println("A operação só pode ser 0,1 ou 2");
                    case "nvacinas" ->
                        System.out.println("O número de vacinas tem de ser inteiro!");
                    case "3" -> {}
                }
            }
        } while (true);
    }

    String inputStr(String campo) {
        Scanner in = new Scanner(System.in);
        String regex = "", s = "";
        do {
            switch (campo) {
                case "tipo" ->
                    System.out.println("Introduza o tipo de vacina: ");
                case "data" ->
                    System.out.println("Introduza a data (dd-mm-aaaa):");
            }
            try {
                switch (campo) {
                    case "tipo" -> s = in.nextLine();
                    case "data" -> {
                        regex = "[0-3][0-9]-[0-1][0-9]-202[0-9]";
                        s = in.next(regex);
                    }
                }
                return s;
            } catch (Exception e) {
                switch (campo) {
                    case "tipo" -> System.out.println("Nome não válido");
                    case "data" -> System.out.println("A data tem de ser do formato dd-mm-aaaa!");
                }
            }
        } while (true);
    }
}
