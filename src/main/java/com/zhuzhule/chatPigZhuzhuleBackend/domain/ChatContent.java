package com.zhuzhule.chatPigZhuzhuleBackend.domain;

import lombok.Data;

/**
 * @TableName chat_content
 */
@Data
public class ChatContent {
  /** */
  private String id;

  /** */
  private String userId;

  /** */
  private String conversationId;

  /** */
  private String content;

  /** */
  private String prepare1;

  /** */
  private String prepare2;

  /** */
  private String prepare3;

  /** bot answer or user answer */
  private String userType;

  private String createdTime;

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
    ChatContent other = (ChatContent) that;
    return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        && (this.getUserId() == null
            ? other.getUserId() == null
            : this.getUserId().equals(other.getUserId()))
        && (this.getConversationId() == null
            ? other.getConversationId() == null
            : this.getConversationId().equals(other.getConversationId()))
        && (this.getContent() == null
            ? other.getContent() == null
            : this.getContent().equals(other.getContent()))
        && (this.getPrepare1() == null
            ? other.getPrepare1() == null
            : this.getPrepare1().equals(other.getPrepare1()))
        && (this.getPrepare2() == null
            ? other.getPrepare2() == null
            : this.getPrepare2().equals(other.getPrepare2()))
        && (this.getPrepare3() == null
            ? other.getPrepare3() == null
            : this.getPrepare3().equals(other.getPrepare3()))
        && (this.getUserType() == null
            ? other.getUserType() == null
            : this.getUserType().equals(other.getUserType()))
        && (this.getCreatedTime() == null
            ? other.getCreatedTime() == null
            : this.getCreatedTime().equals(other.getCreatedTime()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
    result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
    result = prime * result + ((getConversationId() == null) ? 0 : getConversationId().hashCode());
    result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
    result = prime * result + ((getPrepare1() == null) ? 0 : getPrepare1().hashCode());
    result = prime * result + ((getPrepare2() == null) ? 0 : getPrepare2().hashCode());
    result = prime * result + ((getPrepare3() == null) ? 0 : getPrepare3().hashCode());
    result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
    result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
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
    sb.append(", conversationId=").append(conversationId);
    sb.append(", content=").append(content);
    sb.append(", prepare1=").append(prepare1);
    sb.append(", prepare2=").append(prepare2);
    sb.append(", prepare3=").append(prepare3);
    sb.append(", userType=").append(userType);
    sb.append(", createdTime=").append(createdTime);
    sb.append("]");
    return sb.toString();
  }
}
