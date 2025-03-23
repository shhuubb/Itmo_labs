package Commands;



import Utility.ExecutionResponse;

/**
 * Команда exit: завершает программу (без сохранения в файл).
 * @author sh_ub
 */
public class Exit extends Utility.Command {

    public Exit() {
        super("exit ", "завершить программу (без сохранения в файл)");
    }

    @Override
    public ExecutionResponse execute(String arg) {
        if (arg.isEmpty()) return new ExecutionResponse("Illegal number of arguments!", false);

        return new ExecutionResponse("exit", true);

    }
}
