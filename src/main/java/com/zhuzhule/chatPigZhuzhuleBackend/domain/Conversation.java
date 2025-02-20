package com.zhuzhule.chatPigZhuzhuleBackend.domain;

import lombok.Data;

/**
 * @TableName conversation
 */
@Data
public class Conversation {
  /** */
  private String id;

  /** */
  private String userId;

  /** */
  private String createdTime;

  /** */
  private String label;

  /** */
  private String prepare2;

  private String aiType;

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
    Conversation other = (Conversation) that;
    return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        && (this.getUserId() == null
            ? other.getUserId() == null
            : this.getUserId().equals(other.getUserId()))
        && (this.getCreatedTime() == null
            ? other.getCreatedTime() == null
            : this.getCreatedTime().equals(other.getCreatedTime()))
        && (this.getLabel() == null
            ? other.getLabel() == null
            : this.getLabel().equals(other.getLabel()))
        && (this.getPrepare2() == null
            ? other.getPrepare2() == null
            : this.getPrepare2().equals(other.getPrepare2()))
        && (this.getAiType() == null
            ? other.getAiType() == null
            : this.getAiType().equals(other.getAiType()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
    result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
    result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
    result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
    result = prime * result + ((getPrepare2() == null) ? 0 : getPrepare2().hashCode());
    result = prime * result + ((getAiType() == null) ? 0 : getAiType().hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName());
    sb.append(" [");
    sb.append("Hash = ").append(hashCode());
    sb.append(", id=").append(id);
    sb.append(", userId=").append(userId);
    sb.append(", createdtime=").append(createdTime);
    sb.append(", prepare1=").append(label);
    sb.append(", prepare2=").append(prepare2);
    sb.append(", aiType=").append(aiType);
    sb.append("]");
    return sb.toString();
  }
}
