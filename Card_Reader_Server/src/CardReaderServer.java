
public class CardReaderServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/* Commands for communication
		 * XXXYYYREST
		 * XXX == room_id e.g. 101
		 * YYY == function e.g. UPD
		 * REST == data
		 * 
		 * Sending
		 * UPD == Update display for session, - for demo purposes only, wont be in string
		 * REST == Sessioncode(A)-Sessionname(B)-Starttime(C)-Endtime(d)
		 * A== String 11
		 * B== String 30
		 * C== String 8
		 * D== String 8
		 * 
		 * Example:
		 * 101UPDCSY1234/01Test Session                  10:00:0011:00:00
		 * 
		 * 
		 * CON == confirmation of student registration
		 * REST == String 4
		 * PASS
		 * FAIL
		 * 
		 * e.g
		 * 101CONPASS
		 * 
		 * Receiving
		 * REG == student registration
		 * REST == int 8 student_id
		 * 
		 * e.g.
		 * 101REG12345678
		 */
		
		

		//connection to server/table via vagrant
				String urlVagrant= "jdbc:mysql://192.168.56.3:3306/Plan_A";
				String userNameVagrant= "student";
				String passwordVagrant= "student";
		
		//connection to server/table via uni DB
		String url= "jdbc:mysql://194.81.104.22:3306/db_group4_1516";
		String userName= "group4_1516";
		String password= "group4";
		
		//roomSession rs = new roomSession(url, userName, password, 45, 4455, "127.0.0.1" );
		
		//rs.start();
		
		autoAbsent aA = new autoAbsent(urlVagrant, userNameVagrant, passwordVagrant, 45 );
		
		aA.start();
																															
		registration reg = new registration(urlVagrant, userNameVagrant, passwordVagrant, "xxx", 45, 1234, 5678, 10, 10);
		
		reg.start();
	}

}
