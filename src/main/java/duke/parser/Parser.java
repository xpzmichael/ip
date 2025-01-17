package duke.parser;

import duke.commands.*;

import duke.common.NoGoodException;
import duke.common.Type;
import duke.data.Task;

public class Parser {

    /**
     * Parse the user input to a command if the input is valid.
     * @param input The text that the user input.
     **/
    public static Command parse(String input) throws AssertionError {
        assert input.length() < 1000;
        if (input.equals("hi")) {
            return new WelcomeCommand();
        }
        if (input.equals("bye")) {
            return new ExitCommand();
        }
        if (input.equals("list")) {
            return new ListCommand();
        }
        if (input.startsWith("mark ") && (input.length() == 6 || input.length() == 7)) {
            return parseMark(input);
        }
        if ((input.startsWith("delete ") && (input.length() == 8 || input.length() == 9))) {
            return parseDelete(input);
        }
        if ((input.startsWith("find ") && input.length() > 5)) {
            String prefix = input.split("\\s+")[1];
            return new SearchCommand(prefix);
        }
        if (input.startsWith("view schedule ")) {
            return new ViewSchedulesCommand(input.split("\\s+")[2]);
        }
        return parseAdd(input);
    }

    private static Command parseMark(String input) {
        try {
            int i = Integer.parseInt(input.split("\\s+")[1]) - 1;
            return new MarkCommand(i);
        } catch (NumberFormatException e) {
            return new PrintCommand("Please enter a number after mark! (E.g. mark 2)");
        } catch (IndexOutOfBoundsException e) {
            return new PrintCommand("Please enter a valid number!");
        }
    }

    private static Command parseDelete(String input) {
        try {
            int i = Integer.parseInt(input.split("\\s+")[1]) - 1;
            return new DeleteCommand(i);
        } catch (NumberFormatException e) {
            return new PrintCommand("Please enter a number after delete! (E.g. delete 2)");
        } catch (IndexOutOfBoundsException e) {
            return new PrintCommand("Please enter a valid number!");
        }
    }

    private static Command parseAdd(String input) {
        try {
            Task t;
            if (input.startsWith("todo ")) t = new Task(input.split(" ", 2)[1], Type.TODO);
            else if (input.startsWith("event ")) {
                t = new Task(input.split(" /")[0].split(" ", 2)[1], Type.EVENT);
                t.setTime(input.split("/at ", 2)[1]);
            } else if (input.startsWith("deadline ")) {
                t = new Task(input.split(" /")[0].split(" ", 2)[1], Type.DEADLINE);
                t.setTime(input.split("/by ", 2)[1]);
            } else {
                throw new NoGoodException("");
            }
            return new AddCommand(t);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new PrintCommand("Your expression of time is not valid\n" +
                    "eg: /at 2022-01-27 6pm or /by 2022/01/27 6pm");
        } catch (NoGoodException e) {
            return new PrintCommand("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

}
