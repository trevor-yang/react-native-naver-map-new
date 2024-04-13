/**
 * 지도의 유형을 변경하면 가장 바닥에 나타나는 배경 지도의 스타일이 변경됩니다.
 *
 * - Basic: 일반 지도입니다. 하천, 녹지, 도로, 심벌 등 다양한 정보를 노출합니다.
 * - Navi: 차량용 내비게이션에 특화된 지도입니다.
 * - Satellite: 위성 지도입니다. 심벌, 도로 등 위성 사진을 제외한 요소는 노출되지 않습니다.
 * - Hybrid: 위성 사진과 도로, 심벌을 함께 노출하는 하이브리드 지도입니다.
 * - NaviHybrid: 위성 사진과 내비게이션용 도로, 심벌을 함께 노출하는 하이브리드 지도입니다.
 * - Terrain: 지형도입니다. 산악 지형을 실제 지형과 유사하게 입체적으로 표현합니다.
 * - None: 지도를 나타내지 않습니다. 단, 오버레이는 여전히 나타납니다.
 */
export type MapType =
  | 'Basic'
  | 'Navi'
  | 'Satellite'
  | 'Hybrid'
  | 'Terrain'
  | 'NaviHybrid'
  | 'None';
export const MapTypes = [
  'Basic',
  'Navi',
  'Satellite',
  'Hybrid',
  'Terrain',
  'NaviHybrid',
  'None',
] satisfies MapType[];
