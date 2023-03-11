class AlltrackAttribution {
  final String? trackerToken;
  final String? trackerName;
  final String? network;
  final String? campaign;
  final String? adgroup;
  final String? creative;
  final String? clickLabel;
  final String? adid;
  final String? costType;
  final num? costAmount;
  final String? costCurrency;
  // Android only
  final String? fbInstallReferrer;

  AlltrackAttribution({
    required this.trackerToken,
    required this.trackerName,
    required this.network,
    required this.campaign,
    required this.adgroup,
    required this.creative,
    required this.clickLabel,
    required this.adid,
    required this.costType,
    required this.costAmount,
    required this.costCurrency,
    required this.fbInstallReferrer,
  });

  factory AlltrackAttribution.fromMap(dynamic map) {
    try {
      double parsedCostAmount = -1;
      try {
        if (map['costAmount'] != null) {
          parsedCostAmount = double.parse(map['costAmount']);
        }
      } catch (ex) {}

      return AlltrackAttribution(
        trackerToken: map['trackerToken'],
        trackerName: map['trackerName'],
        network: map['network'],
        campaign: map['campaign'],
        adgroup: map['adgroup'],
        creative: map['creative'],
        clickLabel: map['clickLabel'],
        adid: map['adid'],
        costType: map['costType'],
        costAmount: parsedCostAmount != -1 ? parsedCostAmount : null,
        costCurrency: map['costCurrency'],
        fbInstallReferrer: map['fbInstallReferrer'],
      );
    } catch (e) {
      throw Exception(
          '[AlltrackFlutter]: Failed to create AlltrackAttribution object from given map object. Details: ' +
              e.toString());
    }
  }
}
