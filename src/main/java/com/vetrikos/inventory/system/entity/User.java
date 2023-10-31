package com.vetrikos.inventory.system.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String fullName;

  @OneToMany(mappedBy = "createdBy", fetch = FetchType.EAGER)
  @Builder.Default
  private List<Order> orders = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER, cascade = {
      CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
  })
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", fullName='" + fullName + '\'' +
        ", warehouseId=" + (warehouse == null ? null : warehouse.getId()) +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User user)) {
      return false;
    }

    if (!Objects.equals(id, user.id)) {
      return false;
    }
    if (!username.equals(user.username)) {
      return false;
    }
    return fullName.equals(user.fullName);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + username.hashCode();
    result = 31 * result + fullName.hashCode();
    return result;
  }
}
