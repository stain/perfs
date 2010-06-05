package com.googlecode.perfs.transport.azureus;

import java.io.File;
import java.io.InputStream;

import org.gudy.azureus2.plugins.Plugin;
import org.gudy.azureus2.plugins.PluginException;
import org.gudy.azureus2.plugins.PluginInterface;
import org.gudy.azureus2.plugins.download.Download;
import org.gudy.azureus2.plugins.download.DownloadException;
import org.gudy.azureus2.plugins.messaging.Message;
import org.gudy.azureus2.plugins.messaging.MessageException;
import org.gudy.azureus2.plugins.messaging.MessageManager;
import org.gudy.azureus2.plugins.messaging.MessageManagerListener;
import org.gudy.azureus2.plugins.peers.Peer;
import org.gudy.azureus2.plugins.torrent.Torrent;
import org.gudy.azureus2.plugins.torrent.TorrentException;

public class PerFSPlugin implements Plugin {

	private PluginInterface pluginInterface;
	private Torrent rendezVousTorrent;
	private Download rendezVousDownload;

	private final class MessageListener implements
			MessageManagerListener {
		public void compatiblePeerFound(Download download, Peer peer,
				Message message) {
			System.out.println("Woho, I found " + download + " " + peer.getClient() + " " + peer.getIp() + " " + message);
			
		}

		public void peerRemoved(Download download, Peer peer) {
			// TODO Auto-generated method stub
			System.out.println(download.getName());
			System.out.println("He left.." + download + " " + peer.getClient() + " " + peer.getIp()  + " :(");
		}
	}

	public void initialize(PluginInterface pluginInterface)
			throws PluginException {
		this.pluginInterface = pluginInterface;
		System.out.println("Hello from PerFS!");
		MessageManager messageManager = pluginInterface.getMessageManager();
		try {
			messageManager.registerMessageType(PerFSMessage.TEMPLATE);
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageListener listener = new MessageListener();
		messageManager.locateCompatiblePeers(pluginInterface,
				PerFSMessage.TEMPLATE, listener);
		
		InputStream rendezVousStream = getClass().getResourceAsStream("perfs-rendezvous.torrent");
		try {
			rendezVousTorrent = pluginInterface.getTorrentManager().createFromBEncodedInputStream(rendezVousStream );
		} catch (TorrentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pluginDir = pluginInterface.getPluginDirectoryName();
		File rendezVousFile = new File(pluginDir, "rendezvous");
		rendezVousFile.mkdir();
		try {
			rendezVousDownload = pluginInterface.getDownloadManager().addDownload(rendezVousTorrent, null, rendezVousFile);
			rendezVousDownload.setForceStart(true);
		} catch (DownloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
