export interface IManagerMenuItem {
  id?: string;
  icon?: string;
  label: string;
  to: string;
  newWindow?: boolean;
  subs?: IManagerMenuItem[];
}

const data: IManagerMenuItem[] = [{
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
        label: 'View Orders',
        to: '/app/orders/datatables/view-order'
      },
  ]
},
{
  id: 'employees',
  icon: 'iconsminds-business-man-woman',
  label: 'Employees',
  to: '/app/employees',
  subs: [
    {
      id: 'viewEmployee',
      icon: 'simple-icon-grid',
      label: 'View Employees',
      to: '/app/employees/list/view-employees'
    },
    {
      id: 'registerEmployee',
      icon: 'simple-icon-grid',
      label: 'Register Employee',
      to: '/app/employees/forms/register-employee'
    },
    {
      id: 'viewreport',
      icon: 'simple-icon-grid',
      label: 'View Reports',
      to: '/app/employees/datatables/view-report'
    },
    {
      id: 'submitreport',
      icon: 'simple-icon-grid',
      label: 'Submit Report',
      to: '/app/employees/forms/submit-report'
    }
  ]
},
{
  id: 'branch',
  icon: 'iconsminds-office',
  label: 'Branch',
  to: '/app/branch',
  subs: [
    {
      id: 'viewBranch',
      icon: 'iconsminds-office',
      label: 'View Branch',
      to: '/app/branch/datatables/view-branch'
    },
    {
      id: 'viewFleet',
      icon: 'simple-icon-grid',
      label: 'View Fleet',
      to: '/app/fleet/list/view-fleet'
    },
    {
      icon: 'iconsminds-basket-coins',
      label: 'View Stock',
      to: '/app/stock/list/view-stock'
    }
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
