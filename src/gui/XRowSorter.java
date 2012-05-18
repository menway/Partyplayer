package gui;

import java.util.Comparator;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class XRowSorter extends TableRowSorter<TableModel> {
	public XRowSorter() {
		super();
		Comparator<Comparable<?>> comparator = new Comparator<Comparable<?>>() {

			@Override
			public int compare(Comparable<?> o1, Comparable<?> o2) {
				if(o1 instanceof String && o2 instanceof String) {
					String o1String = (String)o1;
					String o2String = (String)o2;
					try {
						int o1Int = Integer.parseInt(o1String);
						int o2Int = Integer.parseInt(o2String);
						return o1Int - o2Int;
					}
					catch (Exception e) {
						return o1String.compareTo(o2String);
					}
				}
				return 0;
			}
			
		};
		for(int i = 0; i < 6; i++)
			setComparator(i, comparator);
	}
}
