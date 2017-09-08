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
@Table(name = "ruang")
public class Ruang implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="gedungid",referencedColumnName="id")
	private Gedung gedung;
	
	@Column(name = "ruang")
	private String ruang;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Gedung getGedung() {
		return gedung;
	}

	public void setGedung(Gedung gedung) {
		this.gedung = gedung;
	}

	public String getRuang() {
		return ruang;
	}

	public void setRuang(String ruang) {
		this.ruang = ruang;
	}
}
