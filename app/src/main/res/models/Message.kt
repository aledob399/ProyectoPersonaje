package models

class Message(private var message: String, private var isReceived: Boolean) {

    fun getMessage(): String {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getIsReceived(): Boolean {
        return isReceived
    }

    fun setIsReceived(isReceived: Boolean) {
        this.isReceived = isReceived
    }
}
