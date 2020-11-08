package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.NetworkWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkWorkerView;

public class NetworkWorkerControl implements BaseControl {

	private NetworkWorkerView _view;
	
	
	private NetworkWorkerModel _model;
	private NetworkNICWorkerControl _niccontrol;
	private NetworkUserWorkerControl _usercontrol;
	private NetworkTapWorkerControl _tapcontrol;
	private NetworkBridgeWorkerControl _bridgecontrol;
	private NetworkTCPSocketWorkerControl _tcpsocketcontrol;
	private NetworkUDPSocketWorkerControl _udpsocketcontrol;
	private NetworkVdeWorkerControl _vdecontrol;
	private NetworkHubportWorkerControl _hubcontrol;
	private NetworkDumpWorkerControl _dumpcontrol;

	public NetworkWorkerControl(EmulationControl _emulation, EmulatorQemuMachineControl _file, int position) {
		this._model = new NetworkWorkerModel(_emulation, _file, position);
		this._view = new NetworkWorkerView(_file, position);
		this._niccontrol = new NetworkNICWorkerControl(_file, this._model, position);
		this._usercontrol = new NetworkUserWorkerControl(_file, this._model, position);
		this._tapcontrol = new NetworkTapWorkerControl(_file, this._model, position);
		this._bridgecontrol = new NetworkBridgeWorkerControl(_file, this._model, position);
		this._tcpsocketcontrol = new NetworkTCPSocketWorkerControl(_file, this._model, position);
		this._udpsocketcontrol = new NetworkUDPSocketWorkerControl(_file, this._model, position);
		this._vdecontrol = new NetworkVdeWorkerControl(_file, this._model, position);
		this._hubcontrol = new NetworkHubportWorkerControl(_file, this._model, position);
		this._dumpcontrol = new NetworkDumpWorkerControl(_file, this._model, position);
	}

	public void starts() {
		this._view.configureStandardMode();
		this._view.configureListener(this);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(_view);
		
		return list;
	}

	public void change_the_visibility_of_view(Boolean value) {
		this._view.setVisible(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("disableButton")) {
			if (this._niccontrol.isSelected() || this._usercontrol.isSelected() || this._tapcontrol.isSelected()
					|| this._bridgecontrol.isSelected() || this._tcpsocketcontrol.isSelected()
					|| this._udpsocketcontrol.isSelected() || this._vdecontrol.isSelected()
					|| this._hubcontrol.isSelected() || this._dumpcontrol.isSelected()) {
				if (this._view.getIsEnabled().isSelected()) {
					this._view.getIsEnabled().setSelected(false);
				}
				String[] options = new String[1];
				options[0] = "";
				this._model.buildIt("-net", options);
			}
			this._view.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			if (!this._niccontrol.isSelected() && !this._usercontrol.isSelected() && !this._tapcontrol.isSelected()
					&& !this._bridgecontrol.isSelected() && !this._tcpsocketcontrol.isSelected()
					&& !this._udpsocketcontrol.isSelected() && !this._vdecontrol.isSelected()
					&& !this._hubcontrol.isSelected() && !this._dumpcontrol.isSelected()
					&& this._view.getIsEnabled().isSelected()) {
				String[] options = new String[1];
				options[0] = "";
				this._model.buildIt("-net", options);
			}
			this._view.setVisible(false);
		} else if (e.getActionCommand().equals("nicOptions")) {
			this._niccontrol.setVisible(true);
		} else if (e.getActionCommand().equals("userOption")) {
			this._usercontrol.setVisible(true);
		} else if (e.getActionCommand().equals("tapOption")) {
			this._tapcontrol.setVisible(true);
		} else if (e.getActionCommand().equals("bridgeOption")) {
			this._bridgecontrol.setVisible(true);
		} else if (e.getActionCommand().equals("tcpSocketOption")) {
			this._tcpsocketcontrol.setVisible(true);
		} else if (e.getActionCommand().equals("udpSocketOption")) {
			this._udpsocketcontrol.setVisible(true);
		} else if (e.getActionCommand().equals("vdeOption")) {
			this._vdecontrol.setVisible(true);
		} else if (e.getActionCommand().equals("hubportOption")) {
			this._hubcontrol.setVisible(true);
		} else if (e.getActionCommand().equals("dumpOption")) {
			this._dumpcontrol.setVisible(true);
		}
	}

	public Boolean isEnabled() {
		return this._view.getIsEnabled().isSelected();
	}

	public void cleanMe() {
		if (this._model.getOption().equals("user")) {
			this._tapcontrol.cleanMe();
			this._bridgecontrol.cleanMe();
			this._tcpsocketcontrol.cleanMe();
			this._udpsocketcontrol.cleanMe();
			this._vdecontrol.cleanMe();
			this._dumpcontrol.cleanMe();
		} else if (this._model.getOption().equals("tap")) {
			this._usercontrol.cleanMe();
			this._bridgecontrol.cleanMe();
			this._tcpsocketcontrol.cleanMe();
			this._udpsocketcontrol.cleanMe();
			this._vdecontrol.cleanMe();
			this._dumpcontrol.cleanMe();
		} else if (this._model.getOption().equals("bridge")) {
			this._tapcontrol.cleanMe();
			this._usercontrol.cleanMe();
			this._tcpsocketcontrol.cleanMe();
			this._udpsocketcontrol.cleanMe();
			this._vdecontrol.cleanMe();
			this._dumpcontrol.cleanMe();
		} else if (this._model.getOption().equals("socket")) {
			this._tapcontrol.cleanMe();
			this._usercontrol.cleanMe();
			this._dumpcontrol.cleanMe();
			this._bridgecontrol.cleanMe();
			this._vdecontrol.cleanMe();
			if (this._tcpsocketcontrol.getDate().after(_udpsocketcontrol.getDate())) {
				this._udpsocketcontrol.cleanMe();
			} else {
				this._tcpsocketcontrol.cleanMe();
			}
		} else if (this._model.getOption().equals("vde")) {
			this._tapcontrol.cleanMe();
			this._usercontrol.cleanMe();
			this._tcpsocketcontrol.cleanMe();
			this._udpsocketcontrol.cleanMe();
			this._dumpcontrol.cleanMe();
			this._bridgecontrol.cleanMe();
		} else if (this._model.getOption().equals("dump")) {
			this._tapcontrol.cleanMe();
			this._usercontrol.cleanMe();
			this._tcpsocketcontrol.cleanMe();
			this._udpsocketcontrol.cleanMe();
			this._bridgecontrol.cleanMe();
			this._vdecontrol.cleanMe();
		}
	}
}
