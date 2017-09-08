package apps.tables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rak")
public class Rak implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="ruangid",referencedColumnName="id")
	private Ruang ruang;
	
	@Column(name = "rak")
	private String rak;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Ruang getRuang() {
		return ruang;
	}

	public void setRuang(Ruang ruang) {
		this.ruang = ruang;
	}

	public String getRak() {
		return rak;
	}

	public void setRak(String rak) {
		this.rak = rak;
	}
}
