package GUI;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    int choice;
    Scanner scanner = new Scanner(System.in);
    public int choosePlaylist (List<String> playlistNames){

        System.out.println("Wybierz playliste:");
        playlistNames.forEach(System.out::println);

        choice = scanner.nextInt();

        return choice;
    }
}
