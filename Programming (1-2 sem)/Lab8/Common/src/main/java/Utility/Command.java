package Utility;

import Command.CommandWithArgs;
/**
 * Абстрактный класс для всех команд
 * @author sh_ub
 */
public abstract class Command{
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Абстрактный метод для выполнения команды
     * @param args аргументы команд
     */
    abstract public ExecutionResponse execute(CommandWithArgs args);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Command command = (Command) obj;
        return name.equals(command.name) && description.equals(command.description);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode();
    }

    @Override
    public String toString() {
        return String.format("command{name = %s, description = %s}", name, description);
    }
}
