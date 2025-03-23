import Command.CommandType;
import Command.CommandWithArgs;

class main {
    public static void main(String[] args) {
        CommandWithArgs command = new CommandWithArgs(CommandType.valueOf("add".toUpperCase()), "");
        System.out.println(command.getCommand());
    }
}