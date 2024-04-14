/**
 * 카메라의 상태를 나타내는 객체입니다.
 */
export interface Camera {
  /** 위도 */
  latitude: number;
  /** 경도 */
  longitude: number;
  /**
   * 카메라의 줌 레벨을 나타내는 속성입니다.<br/>줌 레벨은 지도의 축척을 나타냅니다.
   * 즉, 줌 레벨이 작을수록 지도가 축소되고 클수록 확대됩니다.
   * 줌 레벨이 커지면 지도에 나타나는 정보도 더욱 세밀해집니다.
   *
   * @default 10
   */
  zoom?: number;
  /**
   *  카메라의 기울임 각도를 나타내는 속성입니다.
   *  카메라는 기울임 각도만큼 지면을 비스듬하게 내려다봅니다.
   *  기울임 각도가 0도이면 카메라가 지면을 수직으로 내려다보며, 각도가 증가하면 카메라의 시선도 점점 수평에 가깝게 기울어집니다.
   *  따라서 기울임 각도가 클수록 더 먼 곳을 볼 수 있게 됩니다.
   *  카메라가 기울어지면 화면에 보이는 지도에 원근감이 적용됩니다.
   *  즉, 화면의 중심을 기준으로 먼 곳은 더 작게 보이고 가까운 곳은 더 크게 보입니다.
   *
   *  @default 0
   */
  tilt?: number;
  /**
   * 카메라의 헤딩 각도를 나타내는 속성입니다.
   * 헤딩은 카메라가 바라보는 방위를 의미합니다.
   * 카메라가 정북 방향을 바라볼 때 헤딩 각도는 0도이며, 시계 방향으로 값이 증가합니다.
   * 즉, 동쪽을 바라볼 때 90도, 남쪽을 바라볼 때 180도가 됩니다.
   *
   * @default 0
   */
  bearing?: number;
}
