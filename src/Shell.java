import java.io.*;
import java.text.*;
import java.util.*;

public class Shell {

    private static final String DOCUMENTS_PATH = "V:\\Vinicius\\Documents";
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        Random random = new Random();

        while (true) {
            printMenu();
            int escolha = getChoice();

            switch (escolha) {
                case 1:
                    executeAndSave("tasklist", "output", random.nextInt(1000));
                    break;
                case 2:
                    System.out.println("Insira o processo a ser exibido:");
                    String strProcess = scanner.nextLine().toLowerCase();
                    executeAndSave("tasklist | findstr /i " + strProcess, "output", random.nextInt(1000));
                    break;
                case 3:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Por favor, insira 1, 2 ou 3.");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Listar processos");
        System.out.println("2 - Listar processo personalizado");
        System.out.println("3 - Sair");
    }

    private static int getChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return 0;
        }
    }

    private static void executeAndSave(String command, String prefix, int randomNumber) {
        String output = executeCommand(command);
        System.out.println(output);
        if (askToSaveToFile()) {
            String fileName = generateFileName(prefix, randomNumber);
            saveOutputToFile(output, fileName);
        }
    }

    private static String executeCommand(String command) {
        try {
            Process process = new ProcessBuilder("cmd.exe", "/C", command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            output.append("\nExited with error code: ").append(exitCode).append("\n");
            return output.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Erro ao executar o comando.";
        }
    }

    private static boolean askToSaveToFile() {
        System.out.println("Deseja salvar a saída em um arquivo? (S/N):");
        String choice = scanner.nextLine().trim().toLowerCase();
        return choice.equals("s");
    }

    private static void saveOutputToFile(String output, String fileName) {
        String filePath = DOCUMENTS_PATH + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(output);
            System.out.println("Saída salva em " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateFileName(String prefix, int randomNumber) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String timestamp = dateFormat.format(date);
        return prefix + timestamp + "_" + randomNumber + ".txt";
    }
}
