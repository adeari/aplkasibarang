package apps.tables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "barang")
public class Barang implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name="gedungid",referencedColumnName="id")
	private Gedung gedung;
	
	@OneToOne
	@JoinColumn(name="ruangid",referencedColumnName="id")
	private Ruang ruang;
	
	@OneToOne
	@JoinColumn(name="rakid",referencedColumnName="id")
	private Rak rak;
	
	@Column(name = "barang")
	private String barang;

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

	public Ruang getRuang() {
		return ruang;
	}

	public void setRuang(Ruang ruang) {
		this.ruang = ruang;
	}

	public Rak getRak() {
		return rak;
	}

	public void setRak(Rak rak) {
		this.rak = rak;
	}

	public String getBarang() {
		return barang;
	}

	public void setBarang(String barang) {
		this.barang = barang;
	}
}
