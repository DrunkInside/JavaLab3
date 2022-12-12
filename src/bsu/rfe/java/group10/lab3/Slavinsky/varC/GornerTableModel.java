package bsu.rfe.java.group10.lab3.Slavinsky.varC;
import javax.swing.table.AbstractTableModel;
@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
	
	private Double[] coefficients;
	private Double from;
	private Double to;
	private Double step;
	
	public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.coefficients = coefficients;
	}
	
	public Double getFrom() {
		return from;
	}
	
	public Double getTo() {
		return to;
	}
	
	public Double getStep() {
		return step;
	}
	
	public int getColumnCount() {
	// В данной модели 4 столбца
		return 4;
	}
	public int getRowCount() {
	// Вычислить количество точек между началом и концом отрезка
	// исходя из шага табулирования
		return (int)(Math.ceil((to-from)/step)) + 1;
	}
	
	public Double getValueAt(int row, int col) {
	// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
		double x = from + step * row;
		if (col == 0) {
	// Если запрашивается значение 1-го столбца, то это X
			return x;
		} 
		else if (col == 1){
	// Если запрашивается значение 2-го столбца, то это значение
	// многочлена
			Double result = coefficients[0];
			for(int i = 1; i < coefficients.length; ++i) {
				result *= x;
				result += coefficients[i];
			}
			return result;
		}
		else if (col == 2){
			Double result = coefficients[coefficients.length - 1];
			for(int i = coefficients.length - 2; i >= 0; --i) {
				result *= x;
				result += coefficients[i];
			}
			return result;
		}
		else {
			return getValueAt(row, 1) - getValueAt(row, 2);
		}
	}
		public String getColumnName(int col) {
			switch (col) {
				case 0:
				// Название 1-го столбца
					return "Значение X";
				case 1:
					// Название 1-го столбца
						return "Значение многочлена";
				case 2:
					// Название 1-го столбца
						return "Значение с перевёрнутым";
				default:
				// Название 4-го столбца
					return "Разница";
			}
			}
		public Class<?> getColumnClass(int col) {
			// И в 1-ом и во 2-ом столбце находятся значения типа Double
			return Double.class;
		}
}
