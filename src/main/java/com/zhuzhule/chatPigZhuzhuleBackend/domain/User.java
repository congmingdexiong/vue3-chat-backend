package com.zhuzhule.chatPigZhuzhuleBackend.domain;

import lombok.Data;

/**
 * @TableName user
 */
@Data
public class User {
  /** */
  private String id;

  /** */
  private String nickName;

  /** */
  private String createdTime;

  /** */
  private String status;

  /** */
  private String prepare1;

  /** */
  private String prepare2;

  /** */
  private String prepare3;

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (getClass() != that.getClass()) {
      return false;
    }
    User other = (User) that;
    return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        && (this.getNickName() == null
            ? other.getNickName() == null
            : this.getNickName().equals(other.getNickName()))
        && (this.getCreatedTime() == null
            ? other.getCreatedTime() == null
            : this.getCreatedTime().equals(other.getCreatedTime()))
        && (this.getStatus() == null
            ? other.getStatus() == null
            : this.getStatus().equals(other.getStatus()))
        && (this.getPrepare1() == null
            ? other.getPrepare1() == null
            : this.getPrepare1().equals(other.getPrepare1()))
        && (this.getPrepare2() == null
            ? other.getPrepare2() == null
            : this.getPrepare2().equals(other.getPrepare2()))
        && (this.getPrepare3() == null
            ? other.getPrepare3() == null
            : this.getPrepare3().equals(other.getPrepare3()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
    result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
    result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
    result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
    result = prime * result + ((getPrepare1() == null) ? 0 : getPrepare1().hashCode());
    result = prime * result + ((getPrepare2() == null) ? 0 : getPrepare2().hashCode());
    result = prime * result + ((getPrepare3() == null) ? 0 : getPrepare3().hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", id=").append(id);
    sb.append(", nickName=").append(nickName);
    sb.append(", createdTime=").append(createdTime);
    sb.append(", status=").append(status);
    sb.append(", prepare1=").append(prepare1);
    sb.append(", prepare2=").append(prepare2);
    sb.append(", prepare3=").append(prepare3);
    sb.append("]");
    return sb.toString();
  }
}
