package org.activiti.bpmn.converter.export;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.GraphicInfo;
import org.apache.commons.lang.StringUtils;

public class BPMNDIExport implements BpmnXMLConstants {

  public static void writeBPMNDI(BpmnModel model, XMLStreamWriter xtw) throws Exception {
    // BPMN DI information
    xtw.writeStartElement(BPMNDI_PREFIX, ELEMENT_DI_DIAGRAM, BPMNDI_NAMESPACE);
    
    String processId = null;
    if(model.getPools().size() > 0) {
      processId = "Collaboration";
    } else {
      processId = model.getMainProcess().getId();
    }
    
    xtw.writeAttribute(ATTRIBUTE_ID, "BPMNDiagram_" + processId);

    xtw.writeStartElement(BPMNDI_PREFIX, ELEMENT_DI_PLANE, BPMNDI_NAMESPACE);
    xtw.writeAttribute(ATTRIBUTE_DI_BPMNELEMENT, processId);
    xtw.writeAttribute(ATTRIBUTE_ID, "BPMNPlane_" + processId);
    
    for (String elementId : model.getLocationMap().keySet()) {
      
      if (model.getFlowElement(elementId) != null || model.getArtifact(elementId) != null || 
          model.getPool(elementId) != null || model.getLane(elementId) != null) {
        
        xtw.writeStartElement(BPMNDI_PREFIX, ELEMENT_DI_SHAPE, BPMNDI_NAMESPACE);
        xtw.writeAttribute(ATTRIBUTE_DI_BPMNELEMENT, elementId);
        xtw.writeAttribute(ATTRIBUTE_ID, "BPMNShape_" + elementId);
        
        GraphicInfo graphicInfo = model.getGraphicInfo(elementId);
        xtw.writeStartElement(OMGDC_PREFIX, ELEMENT_DI_BOUNDS, OMGDC_NAMESPACE);
        xtw.writeAttribute(ATTRIBUTE_DI_HEIGHT, "" + graphicInfo.height);
        xtw.writeAttribute(ATTRIBUTE_DI_WIDTH, "" + graphicInfo.width);
        xtw.writeAttribute(ATTRIBUTE_DI_X, "" + graphicInfo.x);
        xtw.writeAttribute(ATTRIBUTE_DI_Y, "" + graphicInfo.y);
        xtw.writeEndElement();
        
        xtw.writeEndElement();
      }
    }
    
    for (String elementId : model.getFlowLocationMap().keySet()) {
      
      if (model.getFlowElement(elementId) != null) {
      
        xtw.writeStartElement(BPMNDI_PREFIX, ELEMENT_DI_EDGE, BPMNDI_NAMESPACE);
        xtw.writeAttribute(ATTRIBUTE_DI_BPMNELEMENT, elementId);
        xtw.writeAttribute(ATTRIBUTE_ID, "BPMNEdge_" + elementId);
        
        List<GraphicInfo> graphicInfoList = model.getFlowLocationGraphicInfo(elementId);
        for (GraphicInfo graphicInfo : graphicInfoList) {
          xtw.writeStartElement(OMGDI_PREFIX, ELEMENT_DI_WAYPOINT, OMGDI_NAMESPACE);
          xtw.writeAttribute(ATTRIBUTE_DI_X, "" + graphicInfo.x);
          xtw.writeAttribute(ATTRIBUTE_DI_Y, "" + graphicInfo.y);
          xtw.writeEndElement();
        }
        
        GraphicInfo labelGraphicInfo = model.getLabelGraphicInfo(elementId);
        FlowElement flowElement = model.getFlowElement(elementId);
        if (labelGraphicInfo != null && flowElement != null && StringUtils.isNotEmpty(flowElement.getName())) {
          xtw.writeStartElement(BPMNDI_PREFIX, ELEMENT_DI_LABEL, BPMNDI_NAMESPACE);
          xtw.writeStartElement(OMGDC_PREFIX, ELEMENT_DI_BOUNDS, OMGDC_NAMESPACE);
          xtw.writeAttribute(ATTRIBUTE_DI_HEIGHT, "" + labelGraphicInfo.height);
          xtw.writeAttribute(ATTRIBUTE_DI_WIDTH, "" + labelGraphicInfo.width);
          xtw.writeAttribute(ATTRIBUTE_DI_X, "" + labelGraphicInfo.x);
          xtw.writeAttribute(ATTRIBUTE_DI_Y, "" + labelGraphicInfo.y);
          xtw.writeEndElement();
          xtw.writeEndElement();
        }
        
        xtw.writeEndElement();
      }
    }
    
    // end BPMN DI elements
    xtw.writeEndElement();
    xtw.writeEndElement();
  }
}
