class AlltrackSessionFailure {
  final String? message;
  final String? timestamp;
  final String? adid;
  final String? jsonResponse;
  final bool? willRetry;

  AlltrackSessionFailure({
    required this.message,
    required this.timestamp,
    required this.adid,
    required this.jsonResponse,
    required this.willRetry,
  });

  factory AlltrackSessionFailure.fromMap(dynamic map) {
    try {
      return AlltrackSessionFailure(
        message: map['message'],
        timestamp: map['timestamp'],
        adid: map['adid'],
        jsonResponse: map['jsonResponse'],
        willRetry: map['willRetry']?.toString().toLowerCase() == 'true',
      );
    } catch (e) {
      throw Exception(
          '[AlltrackFlutter]: Failed to create AlltrackSessionFailure object from given map object. Details: ' +
              e.toString());
    }
  }
}
