import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class ReservationManagerGUI extends WindowAdapter implements MouseListener, ActionListener {

	private Theater theater;
	private LinkedList<Client> clients;

	JFrame frame = new JFrame(ConfigDetails.APPLICATION_NAME);
	JButton newClient = new JButton(ConfigDetails.NEW_CLIENT);
	JButton delClient = new JButton(ConfigDetails.REMOVE_CLIENT); 
	JButton showReservation = new JButton(ConfigDetails.SHOW_RESERVATION);
	JButton makeReservation = new JButton(ConfigDetails.MAKE_RESERVATION);
	JButton cancelReservation = new JButton(ConfigDetails.CANCEL_RESERVATION);
	JComboBox<Client> comboClients = new JComboBox<Client>();

	public ReservationManagerGUI() throws NumberFormatException, InvalidActionException, IOException, ClassNotFoundException {
		File dir = new File("./");
		File[] theaterFile = dir.listFiles(new FilenameFilter() {
			public boolean accept (File dir, String filename) {
				return filename.endsWith(".bak");
			}
		});

		theater = new Theater(theaterFile.length > 0 ? theaterFile[0].getAbsolutePath() : ConfigDetails.THEATER_FILE);

		File clientFile = new File(ConfigDetails.CLIENTS_FILE);
		clients = clientFile.exists() ? Serializer.<LinkedList<Client>>loadFromFile(ConfigDetails.CLIENTS_FILE) : new LinkedList<Client>();

				frame.setMinimumSize(theater.getPreferredSize());
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent windowEvent) {
						int choice = JOptionPane.showConfirmDialog(null, "Do you want to quit ?", "Confirm", JOptionPane.YES_NO_OPTION);
						switch (choice) {
							case JOptionPane.YES_OPTION:
									try {
										theater.save();
									}
									catch (IOException e) {
										e.printStackTrace();
									}
									frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								break;
							default:
									frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
								break;
						}
					}
				});

				frame.pack(); 
				GridLayout buttonLayout = new GridLayout(2, 3);
				JPanel buttonPanel = new JPanel();

				newClient.setActionCommand("ac");
				newClient.addActionListener(this);

				delClient.setActionCommand("rc");
				delClient.addActionListener(this);

				showReservation.setActionCommand("sr");
				showReservation.addActionListener(this);

				makeReservation.setActionCommand("mr");
				makeReservation.addActionListener(this);

				cancelReservation.setActionCommand("cr");
				cancelReservation.addActionListener(this);

				frame.setLayout(new BorderLayout());
				frame.add(theater, BorderLayout.CENTER); 
				refreshCombo();


				buttonPanel.add(comboClients);
				buttonPanel.add(newClient);
				buttonPanel.add(delClient);
				buttonPanel.add(showReservation);
				buttonPanel.add(makeReservation);
				buttonPanel.add(cancelReservation);

				buttonPanel.setLayout(buttonLayout);

				frame.add(buttonPanel, BorderLayout.NORTH);

				theater.addMouseListener(this);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
	}
	
	public static void main(String[] args)  {
			try {
				ReservationManagerGUI rmg = new ReservationManagerGUI();
			} catch (NumberFormatException | ClassNotFoundException | InvalidActionException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void mouseClicked(MouseEvent ev) {
		
		Point p = ev.getPoint();
		theater.setSelectedCol(p.x / theater.getRectSize());
		theater.setSelectedRow(p.y / theater.getRectSize());
		theater.updateUI();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			switch (e.getActionCommand()) {
				case "ac":
						addClient();
					break;
				case "rc":
						removeClient();
					break;
				case "sr":
						showReservation();
					break;
				case "mr":
						makeReservation();
					break;
				case "cr":
						cancelReservation();
					break;
			}
			theater.updateUI();
		}
		catch(IOException | InvalidActionException ex) {
			JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showReservation() throws InvalidActionException {
		Client selectedClient = comboClients.getItemAt(comboClients.getSelectedIndex());

		if(selectedClient != null){	
			JOptionPane.showMessageDialog(null, selectedClient.getExplictedCost(), "Seats of " + selectedClient.toString(), JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			JOptionPane.showMessageDialog(null, "Invalid Client", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void addClient() throws InvalidActionException, IOException {

		JComboBox<String> clientTypeChoice = new JComboBox<>();
		clientTypeChoice.addItem("Client");
		clientTypeChoice.addItem("VIP");
		clientTypeChoice.addItem("Group");
		JTextField lastnameField = new JTextField(5);
		JTextField firstnameField = new JTextField(5);
		JTextField addressField = new JTextField(5);

		JPanel myPanel = new JPanel(new GridLayout(4, 2));
		myPanel.add(new JLabel("Type : "));
		myPanel.add(clientTypeChoice);
		myPanel.add(new JLabel("Lastname : "));
		myPanel.add(lastnameField);
		myPanel.add(new JLabel("Firstname : "));
		myPanel.add(firstnameField);
		myPanel.add(new JLabel("Address : "));
		myPanel.add(addressField);

		JOptionPane.showConfirmDialog(null, myPanel, "Create new Client", JOptionPane.OK_CANCEL_OPTION);

		int nextId = clients.size() > 0 ? clients.getLast().getId() + 1 : 0;

		switch(clientTypeChoice.getItemAt(clientTypeChoice.getSelectedIndex()))
		{
			case "Client" :
				clients.add(new Client(lastnameField.getText(),firstnameField.getText(),addressField.getText()));
				break;
			case "VIP" :
				clients.add(new ClientVIP(lastnameField.getText(),firstnameField.getText(),addressField.getText()));
				break;
			case "Group" :
				clients.add(new ClientGroup(lastnameField.getText(),firstnameField.getText(),addressField.getText()));
				break;
		}
		clients.getLast().setCurrentId(nextId);
		theater.updateUI();
		
		refreshCombo();
		Serializer.saveToFile(ConfigDetails.CLIENTS_FILE, clients);
	}

	public void removeClient() throws InvalidActionException, IOException {

		Client selectedClient = comboClients.getItemAt(comboClients.getSelectedIndex());
		if (selectedClient != null) {
			
			int val = JOptionPane.showConfirmDialog(null, "Effacer ?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
			switch (val) {
			
			case JOptionPane.OK_OPTION:
				
					clients.remove((Client) selectedClient);
					theater.updateUI();
					refreshCombo();
					
					for (Seat s : selectedClient.getSeats()) {
						theater.cancelReservation(s.getCol(), s.getRow());
					}
				break;
				}
			Serializer.saveToFile(ConfigDetails.CLIENTS_FILE, clients);
		}
		else{
			JOptionPane.showMessageDialog(null, "Invalid Client !", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void makeReservation() throws NumberFormatException, InvalidActionException, IOException {
		if(theater.getNbFreeSeats()>0){
			reservationController(true);
		}
		else{
			JOptionPane.showMessageDialog(null, "House full !", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelReservation() throws NumberFormatException, InvalidActionException, IOException {
		if(theater.getNbBookedSeats()>0){
			reservationController(false);
		}
		else{
			JOptionPane.showMessageDialog(null, "No reservation have been made yet.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void reservationController(boolean isBooking) throws NumberFormatException, InvalidActionException, IOException {
		Client selectedClient = comboClients.getItemAt(comboClients.getSelectedIndex()); 
		if (selectedClient != null) {
			if ((theater.getSelectedCol() < theater.getNbCol())
					&& (theater.getSelectedRow() < theater.getNbRow())) {		
				if(isBooking)
				{
					if(theater.makeReservation(theater.getSelectedRow(), theater.getSelectedCol())){
						selectedClient.addSeat(theater.getSeat(theater.getSelectedRow(), theater.getSelectedCol()));
					}
				}
				else
				{
					if(selectedClient.removeSeat(theater.getSeat(theater.getSelectedRow(), theater.getSelectedCol()))){
						theater.cancelReservation(theater.getSelectedRow(), theater.getSelectedCol());
					}
					else{
						JOptionPane.showMessageDialog(null, "This seat is not yours !", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				Serializer.saveToFile(ConfigDetails.CLIENTS_FILE, clients);
			}
			theater.updateUI();
		}
		else{
			JOptionPane.showMessageDialog(null, "Invalid Client !", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void refreshCombo() {

		comboClients.removeAllItems();

		for (Client c : clients){
			comboClients.addItem(c);
		}
		
		comboClients.setActionCommand("combo");
		comboClients.addActionListener(this);
		comboClients.updateUI();
	}
}
