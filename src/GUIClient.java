import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class GUIClient extends JFrame implements ActionListener {
	JTextField businessName, businessID, numOfItems;
	JComboBox<String> items;
	JLabel errormsg;
	JButton send, disconnect;
	Socket socket;
	PrintWriter out;
	BufferedReader in;

	public GUIClient() {
		businessName = new JTextField(15);
		businessID = new JTextField(5);
		items = new JComboBox<>(new String[] { "Sun glasses", "Belt", "Scarf" });
		numOfItems = new JTextField(5);
		disconnect = new JButton("Disconnect");
		send = new JButton("Send");
		errormsg = new JLabel("");

		disconnect.setVisible(false);
		this.setLayout(new FlowLayout());

		this.add(new JLabel("Business Name:"));
		this.add(businessName);
		this.add(new JLabel("Business ID (5 digits):"));
		this.add(businessID);
		this.add(new JLabel("Select Item:"));
		this.add(items);
		this.add(new JLabel("Quantity:"));
		this.add(numOfItems);
		this.add(send);
		this.add(disconnect);
		this.add(errormsg);

		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		send.addActionListener(this);
		disconnect.addActionListener(this);

		try {
			socket = new Socket("127.0.0.1", 9999);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send) {
			String id = businessID.getText();
			String qunt = numOfItems.getText();

			for (int i = 0; i < id.length(); i++) {
				if (!Character.isDigit(id.charAt(i))) {
					errormsg.setForeground(Color.RED);
					errormsg.setText("businessID must be numbers");
					return;
				}
			}

			if (id.length() != 5) {
				errormsg.setForeground(Color.RED);
				errormsg.setText("businessID must be 5 digits");
				return;
			}

			for (int i = 0; i < qunt.length(); i++) {
				if (!Character.isDigit(qunt.charAt(i))) {
					errormsg.setForeground(Color.RED);
					errormsg.setText("Quantity of item must be numbers");
					return;
				}
			}

			try {
				String data = businessName.getText() + ":" + id + ":" + (items.getSelectedIndex() + 1) + ":" + qunt;
				out.println(data);

				String response = in.readLine();

				if (response != null && response.equals("100")) {
					disconnect.setVisible(true);
					errormsg.setForeground(Color.green);
					errormsg.setText("Items sent successfully");
				} else {
					errormsg.setForeground(Color.RED);
					errormsg.setText(response);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} else if (e.getSource() == disconnect) {
			try {
				out.println("EXIT");
				out.close();
				in.close();
				socket.close();
				this.dispose();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}