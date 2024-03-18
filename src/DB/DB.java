package DB;

import java.sql.*;
public class DB {
    private Connection con;
    private Statement statement;

//    public DBoperator(Connection c){
//        this.con = c;
//        try{
//            con.setAutoCommit(false);
//            this.statement = c.createStatement();
//        }catch(Exception e){
//            System.out.println("Problema la constructorul obiect DBoperator!");
//        }
//    }

    public boolean isEmpty(){
        try{
            ResultSet rs = statement.executeQuery("select count(*) from GAME_INFO");
            int rez = rs.getInt(1);
            if(rez > 0)
                return false;
            if(rez == 0)
                return true;
            con.commit();
        }catch(Exception e)
        {
            System.out.println("Problema la functia isEmpty() !");
        }
        return false;
    }

    public void create_table()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //C:\PAOO-PROJ\RADION_CIPRIAN_1210B_PROIECT\Conquest of the Terra\out\production\Conquest of the Terra

        //String url = "jdbc:sqlite:C:\PAOO-PROJ\\RADION_CIPRIAN_1210B_PROIECT\\Conquest of the Terra\\out\\production\\Conquest of the Terra\\tiobe1.db";
        String url = "jdbc:sqlite:tiobe2.db";


        try (Connection conn = DriverManager.getConnection(url))
        {
            String sql = "CREATE TABLE IF NOT EXISTS SCORE1 (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + " SCORE integer NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
            sql = "INSERT OR IGNORE INTO SCORE1(ID,SCORE) " +
                    "VALUES (1, 0 );";
            stmt.execute(sql);
            stmt.close();
            conn.close();



        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    public int getScore(){
        String url = "jdbc:sqlite:tiobe2.db";

        try (Connection conn = DriverManager.getConnection(url))
        { Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT SCORE FROM SCORE1 WHERE ID=1;" );
            int score = rs.getInt("score");



            rs.close();
            stmt.close();
            conn.close();
            return  score;


        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }
    public void setScore(int score) {
        String url = "jdbc:sqlite:tiobe2.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);

            String sql = "UPDATE SCORE1 set SCORE =" + score + " where ID=1;";
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
            conn.close();


        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
