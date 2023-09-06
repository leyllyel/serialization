import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        GameProgress game1 = new GameProgress(100, 3, 5, 150.5);
        GameProgress game2 = new GameProgress(80, 2, 3, 120.0);
        GameProgress game3 = new GameProgress(50, 1, 1, 75.2);


        String savegamesFolderPath = "C:/Users/User/Downloads/Games/savegames";

        saveGame(savegamesFolderPath + "/save1.dat", game1);
        saveGame(savegamesFolderPath + "/save2.dat", game2);
        saveGame(savegamesFolderPath + "/save3.dat", game3);


        String zipFilePath = "C:/Users/User/Downloads/Games/savegames/zip.zip";


        List<String> filesToPack = new ArrayList<>();
        filesToPack.add(savegamesFolderPath + "/save1.dat");
        filesToPack.add(savegamesFolderPath + "/save2.dat");
        filesToPack.add(savegamesFolderPath + "/save3.dat");


        zipFiles(zipFilePath, filesToPack);


        deleteSaveFiles(savegamesFolderPath, filesToPack);
    }

    private static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Игровой прогресс сохранен: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipFiles(String zipFilePath, List<String> filesToPack) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            byte[] buffer = new byte[1024];

            for (String file : filesToPack) {
                File currentFile = new File(file);

                try (FileInputStream fis = new FileInputStream(currentFile)) {
                    zos.putNextEntry(new ZipEntry(currentFile.getName()));

                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                    System.out.println("Файл упакован: " + file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Архив создан: " + zipFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteSaveFiles(String savegamesFolderPath, List<String> filesToKeep) {
        File savegamesFolder = new File(savegamesFolderPath);
        File[] files = savegamesFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!filesToKeep.contains(file.getAbsolutePath())) {
                    boolean deleted = file.delete();
                    System.out.println("Файл удален: " + file.getAbsolutePath() + ", Удален: " + deleted);
                }
            }
        }
    }
}