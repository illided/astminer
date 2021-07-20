package pipeline

import java.lang.RuntimeException

class DownloadException(message: String): RuntimeException(message) {}