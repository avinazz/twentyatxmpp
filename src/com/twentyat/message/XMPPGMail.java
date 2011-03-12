package com.twentyat.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;

/**
 * This class is used for connecting to XMPP server and manage chat
 * 
 * 
 * @author Bipin Sutariya
 *
 */
public class XMPPGMail implements MessageListener {
        private static final String HOSTNAME="cloud07";
	XMPPConnection connection;

	public void login(String userName, String password) throws XMPPException {
            SmackConfiguration.setPacketReplyTimeout(1000);
          //  ConnectionConfiguration config = new ConnectionConfiguration("ec2-184-72-184-211.compute-1.amazonaws.com", 5222, "twentyat");
            ConnectionConfiguration config = new ConnectionConfiguration(HOSTNAME, 5222, "twentyat");
            config.setSASLAuthenticationEnabled(false);
            config.setSendPresence(true);
            System.out.println("Roster set? "+config.isRosterLoadedAtLogin());
        connection = new XMPPConnection(config);

            connection.connect();
            connection.login(userName, password);

	}

	public void sendMessage(String message, String to) throws XMPPException {
		Chat chat = connection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}

	public void displayBuddyList() {
            Roster roster = connection.getRoster();
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

            roster.addRosterListener(new RosterListener() {

                public void entriesAdded(Collection<String> clctn) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void entriesUpdated(Collection<String> clctn) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void entriesDeleted(Collection<String> clctn) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void presenceChanged(Presence prsnc) {
                    prsnc.setStatus(Presence.Mode.available.toString());
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            Collection<RosterEntry> entries = roster.getEntries();

            System.out.println("\n\n" + entries.size() + " buddy(ies):");
            for (RosterEntry r : entries) {
                    System.out.println(r.getUser());
            }
	}

	public void disconnect() {
		connection.disconnect();
	}

        public void createUser(String userName, String password, String name, String email) {
            
        }

        public void createRoster(String user, String name) {
            try {
                System.out.println("Before creating roster");
            Roster roster = connection.getRoster();
                System.out.println("After connection");
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                System.out.println("subscription set");
            roster.createEntry(user, name, new String[] {});
                System.out.println("After creating roster");
            
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

	public void processMessage(Chat chat, Message message) {
		if (message.getType() == Message.Type.chat)
			System.out.println(chat.getParticipant() + " says: "
					+ message.getBody());
	}

	public static void main(String args[]) throws XMPPException, IOException {
		// declare variables
		XMPPGMail c = new XMPPGMail();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg;

		// turn on the enhanced debugger
		XMPPConnection.DEBUG_ENABLED = true;

		// Enter your login information here
		c.login("avinash", "cloud");
                //c.login("avinazz", "letmein");
                //c.login("riddhish", "LETMEIN");

		c.displayBuddyList();

		System.out.println("-----");

		System.out.println("Who do you want to talk to? - Type contacts full email address:");
		String talkTo = br.readLine();
                //String appendedTalkTo = talkTo+"@ec2-184-72-184-211.compute-1.amazonaws.com";
                String appendedTalkTo=talkTo+"@"+HOSTNAME;
                
//                try {
//                    RosterPacket rosterPacket = new RosterPacket();
//                    RosterPacket.Item rpItem = new RosterPacket.Item("himanshu", "kalpesh");
//                    rpItem.setItemType(RosterPacket.ItemType.both);
//                    rpItem.setItemStatus(RosterPacket.ItemStatus.SUBSCRIPTION_PENDING);
//                    System.out.println("Before adding roster");
//                    rosterPacket.addRosterItem(rpItem);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
                System.out.println("All messages will be sent to " + appendedTalkTo);
		System.out.println("Enter your message in the console:");
		System.out.println("-----\n");

		while (!(msg = br.readLine()).equals("bye")) {
			c.sendMessage(msg, appendedTalkTo);
		}

		c.disconnect();
		System.exit(0);
	}

}
