export interface IConsultantMenuItem {
  id?: string;
  icon?: string;
  label: string;
  to: string;
  newWindow?: boolean;
  subs?: IConsultantMenuItem[];
}

const data: IConsultantMenuItem[] = [{
  id: 'routes',
  icon: 'iconsminds-three-arrow-fork',
  label: 'Routes',
  to: '/app/routes',
  subs: [
   {
    icon: 'simple-icon-map',
    label: 'menu.maps',
    to: '/app/routes/components/maps'
  },
  {
    icon: 'simple-icon-pie-chart',
    label: 'menu.analytics',
    to: '/app/routes/components/analytics'
  },
  ]
},
{
  id: 'orders',
  icon: 'iconsminds-box-with-folders',
  label: 'Orders',
  to: '/app/orders',
  subs: [
    {
      icon: 'simple-icon-notebook',
      label: 'Submit Order',
      to: '/app/orders/forms/submit-order'
    },
      {
        icon: 'simple-icon-notebook',
        label: 'View Orders',
        to: '/app/orders/datatables/view-order'
      },
  ]
},
{
  id: 'information',
  icon: 'iconsminds-information',
  label: 'Information',
  to: '/app/information'
}
];
export default data;
