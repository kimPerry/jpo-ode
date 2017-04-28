package us.dot.its.jpo.ode.vsdm;

import us.dot.its.jpo.ode.j2735.dsrc.BasicSafetyMessage;
import us.dot.its.jpo.ode.j2735.dsrc.VehicleData;
import us.dot.its.jpo.ode.j2735.semi.VehSitDataMessage;
import us.dot.its.jpo.ode.j2735.semi.VehSitRecord;
import us.dot.its.jpo.ode.j2735.semi.VehicleSituationStatus;

public class VsdToBsmConverter {
	
	public VsdToBsmConverter() {
		// TODO empty constructor
	}
	
	public BasicSafetyMessage[] convertToBsm(VehSitDataMessage vsdm) {
		
		for (VehSitRecord vsr : vsdm.getBundle().elements) {
			BasicSafetyMessage bsm = new BasicSafetyMessage();
			bsm.coreData.setId(vsr.tempID);
		}
		
//		msgCnt MsgCount,
//		id TemporaryID,
//		secMark DSecond,
//		lat Latitude,
//		long Longitude,
//		elev Elevation,
//		accuracy PositionalAccuracy,
//		transmission TransmissionState,
//		speed Speed,
//		heading Heading,
//		angle SteeringWheelAngle,
//		accelSet AccelerationSet4Way,
//		brakes BrakeSystemStatus,
//		size VehicleSize
		
		
		
		
		
		
		
		
		return null;
		
	}
	
	

}
