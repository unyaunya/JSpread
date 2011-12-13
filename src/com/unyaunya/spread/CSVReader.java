package com.unyaunya.spread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
    private ArrayList<ArrayList<String>> rowArrayList;
    private ArrayList<String> titleArrayList;
    private boolean colTitleSource = false;
    
    /** Creates a new instance of CSVReader
	 * @param colTitleSource  whether or not to read the first line of CSV as column titles
	 */
    public CSVReader(boolean colTitleSource) {
    	this.colTitleSource = colTitleSource;
    }

    public CSVReader() {
    	this(false);
    }

    public boolean getColTitleSource() {
    	return this.colTitleSource;
    }
    public ArrayList<String> getTitleArrayList() {
    	return this.titleArrayList;
    }
    public ArrayList<ArrayList<String>> getRowArrayList() {
    	return this.rowArrayList;
    }
    
    /* Read File */
    public void read(File csvFile) throws FileNotFoundException, IOException {
        BufferedReader bReader = new BufferedReader( new FileReader( csvFile ) );
        read(bReader);
        bReader.close();
    }

    public void read(BufferedReader bReader)
			throws IOException {
        this.rowArrayList = new ArrayList<ArrayList<String>>();
        this.titleArrayList = new ArrayList<String>();
		ArrayList<String> colArrayList = new ArrayList<String>();
        String line;
        
        line = bReader.readLine();
        if( this.colTitleSource == true ) {
            digestLine(line, titleArrayList ); // Get the title from a CSV File (first line)
        }
        else {
            digestLine(line, colArrayList ); // Treat the first line of CSV as the first line of data
            for( int i = 0; i < colArrayList.size(); i++ ) // Empty Title
                titleArrayList.add( null );
            rowArrayList.add( colArrayList );
        }
       
        line = bReader.readLine();
        
        /* Read through each line of CSV File */
        while( true ) {
            if( line == null )
                break;
            
            colArrayList = new ArrayList<String>();
            digestLine(line, colArrayList );
            rowArrayList.add( colArrayList );
            
            line = bReader.readLine();
        }
	}

    /* Read each line of CSV File */
    private void digestLine(
    	String line,
        ArrayList<String> ArrayList // A ArrayList to store the columns extracted from a CSV File line
    ) {
        String cols[];
        
        /*
            Differenciate between "" and ". In a CSV File, "" represents a
            single ", while " is an enclosing character of a String that
            contains a comma character.
         */
        line = line.replaceAll( "\"\"", "<two-double-quotes>" );
        cols = line.split( ",", -1 );

        for( int i = 0; i < cols.length; i++ ) {
            /*
                When a single double-quote is found, append with the next
                columns continuously until a closing single double-quote is
                found
             */
            try {
	            if( cols[i].indexOf( "\"" ) != -1 && cols[i].indexOf( "\"" ) == cols[i].lastIndexOf( "\"" ) ) {
	                String appendedCol = cols[i].replace( "\"", "" );
	                do {
	                    appendedCol += "," + cols[++i].replace( "\"", "" );
	                }
	                while( cols[i].indexOf( "\"" ) == -1 );
	
	                ArrayList.add( appendedCol.replaceAll( "<two-double-quotes>", "\"" ) ); // Replace <two-double-quotes> with a single double-quote (")
	            }
	            else {
	                ArrayList.add( cols[i].replaceAll( "\"", "" ).replaceAll( "<two-double-quotes>", "\"" ) ); // Replace <two-double-quotes> with a single double-quote (")
	            }
            }
            catch( ArrayIndexOutOfBoundsException e ) {
                System.out.println( line );
                System.out.println( cols[i] );
            }
        }
    }
}
