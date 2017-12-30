package lbms.azcatdest.main;

import java.io.File;
import com.biglybt.pif.download.Download;
import com.biglybt.pif.download.DownloadException;
import com.biglybt.pif.torrent.TorrentAttribute;

public class AzCatDestDownloadListener implements com.biglybt.pif.download.DownloadListener {

	static private AzCatDestDownloadListener instance = new AzCatDestDownloadListener();
	static private TorrentAttribute ta = Plugin.getPluginInterface().getTorrentManager().getAttribute(TorrentAttribute.TA_CATEGORY);

	private AzCatDestDownloadListener() {}

	public static AzCatDestDownloadListener getInstance() {
		return instance;
	}

	@Override
	public void positionChanged(Download download, int oldPosition, int newPosition) {
	}

	@Override
	public void stateChanged(final Download download, int old_state, int new_state) {
		if (old_state == Download.ST_DOWNLOADING && download.isComplete()) {
			String cat = download.getAttribute(ta);
			download.removeListener(instance);		//remove listener so it wont be triggered twice
			if (cat != null) { //a Category was
				try {
					String dest = Plugin.getProperties().getProperty(cat, null);
					if (dest!=null) {
						if(!dest.equalsIgnoreCase("")) {
							File destFile = new File(dest);
							if (!destFile.exists())destFile.mkdirs();
							download.moveDataFiles(destFile);
						}
					}
				} catch (DownloadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
