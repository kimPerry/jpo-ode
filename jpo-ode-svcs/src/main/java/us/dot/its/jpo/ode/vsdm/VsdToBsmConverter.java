package us.dot.its.jpo.ode.vsdm;

import java.util.ArrayList;

import us.dot.its.jpo.ode.j2735.dsrc.BasicSafetyMessage;
import us.dot.its.jpo.ode.j2735.semi.VehSitDataMessage;
import us.dot.its.jpo.ode.j2735.semi.VehSitRecord;

public class VsdToBsmConverter {
	
	public VsdToBsmConverter() {
		// TODO empty constructor
	}
	
	public ArrayList<BasicSafetyMessage> convertToBsm(VehSitDataMessage vsdm) {
		
		ArrayList<BasicSafetyMessage> bsms = new ArrayList<>();
		
		for (VehSitRecord vsr : vsdm.getBundle().elements) {
			
			BasicSafetyMessage bsm = new BasicSafetyMessage();
			
			bsm.coreData.id = vsr.tempID;
			bsm.coreData._long = vsr.pos._long;
			bsm.coreData.lat = vsr.pos.lat;
			bsm.coreData.secMark = vsr.time.second;
			bsm.coreData.elev = vsr.pos.elevation;
			bsm.coreData.transmission = vsr.fundamental.speed.transmisson;
			bsm.coreData.heading = vsr.fundamental.heading;
			bsm.coreData.angle = vsr.fundamental.steeringAngle;
			bsm.coreData.accelSet = vsr.fundamental.accelSet;
			bsm.coreData.brakes = vsr.fundamental.brakes;
			bsm.coreData.size = vsr.fundamental.vehSize;
			
			bsms.add(bsm);
			
		}
		
		return bsms;
		
	}
	
	

}
