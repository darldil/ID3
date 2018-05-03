package ui;

import javax.swing.JFrame;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import logica.Nodo;

public class ResultWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResultWindow(Nodo resultado) {
		super("ID3 - Mauricio Abbati Loureiro");

		mxGraph graph = new mxGraph();
		com.mxgraph.swing.util.mxGraphTransferable.enableImageSupport = false;
		graph.setCellsDisconnectable(false);
		graph.setDisconnectOnMove(false);
		graph.setCellsEditable(false);
		/*graph.setCellsMovable(false);
		graph.setEdgeLabelsMovable(false);*/
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			Object v1 = graph.insertVertex(graph.getDefaultParent(), null, resultado.getNombre(), 50, 50, 80,
					30);
			inserccionRecursiva(resultado, graph, v1);
			mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.setUseBoundingBox(false);

            layout.execute(parent);
		}
		finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
	}
	
	private void inserccionRecursiva(Nodo nodo, mxGraph graph, Object parent) {
		if (nodo.getListaNodosSig() != null) {
			for (Nodo nodoSiguiente: nodo.getListaNodosSig()) {
				Object v2 = graph.insertVertex(graph.getDefaultParent(), null, nodoSiguiente.getNombre(), 50, 50, 65,
						30);
				graph.insertEdge(graph.getDefaultParent(), null, nodoSiguiente.getArista(), parent, v2);
				inserccionRecursiva(nodoSiguiente, graph, v2);
			}
		}
	}

}
