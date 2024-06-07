import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class LazyParser {
    Scanner scanner;
    ArrayList<Line> lines;
    ArrayList<ParserModule> parserModules;

    LazyParser(File file) {
        lines = new ArrayList<Line>();
        parserModules = new ArrayList<ParserModule>();
        
        // set up desired parser modules
        ParserModule toolpath = new ToolpathParser();
        parserModules.add(toolpath);

        try {
            scanner = new Scanner(file);
        } catch (IOException e) {
            System.out.println("Loading file failed");
            // exit
        }
        parseGcodeFile();
    }

    void parseGcodeFile() {
        String nextLineStr;
        String[] words;
        boolean lineIsComment;
        Line nextLine;
        GCodeCommand cmd;
        while (scanner.hasNextLine()) {
            nextLineStr = scanner.nextLine();
            if (!nextLineStr.trim().isEmpty()) {
                lineIsComment = (nextLineStr.substring(0,1).equals(";"));
                words = nextLineStr.split("[\\s]");
                if (lineIsComment) {
                    cmd = null;
                } else {
                    cmd = new GCodeCommand(words[0]);
                }
                nextLine = new Line(lineIsComment, cmd, 
                // Always skip first word in string bc it is either command or ";"
                new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(words, 1, words.length))));
                lines.add(nextLine);
                ParserModule currentModule;
                for (int i = 0; i < parserModules.size(); i++) {
                    currentModule = parserModules.get(i);
                    currentModule.parseLine(nextLine);
                }
            }
        }
    }

    
}