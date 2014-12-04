package chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextGrabber
{
    private String filePath;
    private File file;

    public TextGrabber(String fileName)
    {
        this.filePath = fileName;
        this.file = new File(fileName);
    }

    public ArrayList<String> getLines()
    {
        try
        {
            String inputLine;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            ArrayList<String> lines = new ArrayList<String>();

            while ((inputLine = br.readLine()) != null)
            {
                lines.add(inputLine);
            }

            return lines;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getLine(int line)
    {
        try
        {
            String inputLine;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            int cur = 0;

            while ((inputLine = br.readLine()) != null)
            {
                if (cur == line)
                {
                    return inputLine;
                }

                cur++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
