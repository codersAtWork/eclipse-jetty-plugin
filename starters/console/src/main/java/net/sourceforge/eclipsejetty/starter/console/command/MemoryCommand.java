// Licensed under the Apache License, Version 2.0 (the "License");
package net.sourceforge.eclipsejetty.starter.console.command;

import net.sourceforge.eclipsejetty.starter.console.AbstractCommand;
import net.sourceforge.eclipsejetty.starter.console.ConsoleAdapter;
import net.sourceforge.eclipsejetty.starter.console.ArgumentException;
import net.sourceforge.eclipsejetty.starter.console.Process;
import net.sourceforge.eclipsejetty.starter.console.util.MemoryUtils;
import net.sourceforge.eclipsejetty.starter.util.Utils;

public class MemoryCommand extends AbstractCommand
{

    public MemoryCommand(ConsoleAdapter consoleAdapter)
    {
        super(consoleAdapter, "memory", "m");
    }

    public String getFormat()
    {
        return "[gc]";
    }

    public String getDescription()
    {
        return "Memory utilities.";
    }

    @Override
    protected String getHelpDescription()
    {
        return "Prints memory information to the console. If invoked with the gc command, it "
            + "performs a garbage collection.";
    }

    public int getOrdinal()
    {
        return 500;
    }

    public int execute(String processName, Process process)
    {
        String command = process.args.consumeString();

        long freeMemory = MemoryUtils.printMemoryUsage(process.out);

        if (command == null)
        {
            return 0;
        }

        if ("gc".equalsIgnoreCase(command))
        {
            return gc(process, freeMemory);
        }

        throw new ArgumentException("Invalid command: " + command);
    }

    private int gc(Process process, long freeMemory)
    {
        process.out.println();
        process.out.print("Performing GC...");

        long millis = System.nanoTime();

        System.gc();

        process.out.printf(" [%s]\n", Utils.formatSeconds((System.nanoTime() - millis) / 1000000000d));
        process.out.println();

        long newFreeMemory = MemoryUtils.printMemoryUsage(process.out);

        process.out.println();
        process.out.printf("Saved Memory:      %13s\n", Utils.formatBytes(newFreeMemory - freeMemory));

        return 0;
    }

}
