package travelsync;

import travelsync.cli.ConsoleColors;
import travelsync.cli.MenuHandler;
import travelsync.cli.TerminalUI;
import travelsync.planner.TravelSyncEngine;

import java.util.Scanner;

/**
 * Main Application Entry Point for TravelSync CLI.
 * Executable from Eclipse Console and Terminal.
 */
public class Main {

    public static void main(String[] args) {
        // Optional flag to run automated end-to-end demo without prompt
        boolean autoDemo = args.length > 0 && ("--demo".equalsIgnoreCase(args[0]) || "-d".equalsIgnoreCase(args[0]));

        TerminalUI.printBanner();

        try {
            System.out.println(ConsoleColors.cyan("Initializing TravelSync Core Engine & Ingesting Datasets..."));
            TravelSyncEngine engine = new TravelSyncEngine();
            System.out.println(ConsoleColors.brightGreen("✓ Ingestion Completed: ") +
                    engine.getRepository().getAllDestinations().size() + " Destinations, " +
                    engine.getRepository().getAllTransportConnections().size() + " Transport Routes Loaded.");

            if (autoDemo) {
                System.out.println(ConsoleColors.yellow("\nRunning automated TravelSync full pipeline demonstration...\n"));
                Scanner scanner = new Scanner(System.in);
                MenuHandler handler = new MenuHandler(engine, scanner);
                handler.handleOption(10);
                return;
            }

            Scanner scanner = new Scanner(System.in);
            MenuHandler handler = new MenuHandler(engine, scanner);

            boolean running = true;
            while (running) {
                handler.displayMainMenu();
                int choice = TerminalUI.readIntChoice(scanner, "Enter your choice (0-12): ", 0, 12);
                if (choice == 0) {
                    handler.handleOption(0);
                    running = false;
                } else {
                    handler.handleOption(choice);
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal Error during TravelSync execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
